package org.itkk.udf.starter.queue.db;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.itkk.udf.starter.core.exception.handle.IExceptionAlert;
import org.itkk.udf.starter.core.trace.IDataHis;
import org.itkk.udf.starter.queue.db.entity.DbQueueEntity;
import org.itkk.udf.starter.queue.db.service.DbQueueService;
import io.micrometer.core.instrument.util.NamedThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * db队列
 */
@Slf4j
public class DbQueue implements IDbQueue {

    /**
     * 死信队列名称
     */
    private final String dbQueueDeadLetterName = "DB_QUEUE_DEAD_LETTER";

    /**
     * 队列名称
     */
    private final String queueName;

    /**
     * 重试次数
     */
    private final Integer retryCount;

    /**
     * 任务队列
     */
    private final LinkedBlockingQueue<String> queue;

    /**
     * 线程池
     */
    private final ExecutorService executorService;

    /**
     * 信号量
     */
    private final Semaphore semaphore;

    /**
     * 是否开始消费
     */
    private final AtomicBoolean startConsume;

    /**
     * 处理计数器
     */
    private final Map<String, AtomicInteger> handleCounter = new ConcurrentHashMap<>();

    /**
     * dbQueueService
     */
    @Autowired
    private DbQueueService dbQueueService;

    /**
     * iDbQueueLock
     */
    @Autowired
    private IDbQueueLock iDbQueueLock;

    /**
     * 异常通知
     */
    @Autowired(required = false)
    private IExceptionAlert iExceptionAlert;

    /**
     * iDataHis
     */
    @Autowired(required = false)
    private IDataHis iDataHis;

    /**
     * 构造函数
     *
     * @param queueName      队列名称
     * @param maxConcurrency 最大并发数
     */
    public DbQueue(String queueName, int maxConcurrency) {
        this(queueName, maxConcurrency, 0);
    }

    /**
     * 构造函数
     *
     * @param queueName      队列名称
     * @param maxConcurrency 最大并发数
     * @param retryCount     重试次数
     */
    public DbQueue(String queueName, int maxConcurrency, Integer retryCount) {
        //初始化成员变量
        this.queueName = queueName;
        this.retryCount = retryCount;
        this.queue = new LinkedBlockingQueue<>(maxConcurrency);
        this.semaphore = new Semaphore(maxConcurrency);
        this.startConsume = new AtomicBoolean(false);
        //初始化线程池
        final long keepAliveTime = 0;
        final int poolSize = maxConcurrency + 1; //加一是包含了消费线程
        this.executorService = new ThreadPoolExecutor(
                poolSize,
                poolSize,
                keepAliveTime,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(poolSize * 2),
                new NamedThreadFactory(queueName),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
    }

    @PostConstruct
    public void init() {
        //自动开始消费
        this.startConsume();
        //日志输出
        log.info("{} 队列{}开始消费", CoreUtil.getTraceId(), this.queueName);
    }

    @Override
    public String producer(DbQueueMessage dbQueueMessage) {
        //校验
        if (dbQueueMessage == null) {
            throw new ParameterValidException("消息不能为空");
        }
        if (StringUtils.isBlank(dbQueueMessage.getHanledBeanName())) {
            throw new ParameterValidException("处理器bean名称不能为空");
        }
        if (StringUtils.isBlank(dbQueueMessage.getBody())) {
            throw new ParameterValidException("消息体不能为空");
        }
        //消息入库
        dbQueueService.add(this.queueName, dbQueueMessage);
        //放入队列
        boolean success = this.queue.offer(dbQueueMessage.getId());
        //日志输出
        if (!success) {
            log.debug("{} 放入内存队列失败", CoreUtil.getTraceId());
        }
        //返回
        return dbQueueMessage.getId();
    }

    /**
     * 开始消费
     */
    protected void startConsume() {
        //状态判断
        if (this.startConsume.get()) {
            throw new ParameterValidException("此队列已经开始消费");
        }
        //设置消费状态
        this.startConsume.set(true);
        //启动消费线程
        this.executorService.execute(() -> {
            try {
                this.consume();
            } catch (InterruptedException e) {
                throw new SystemRuntimeException(e);
            }
        });
    }

    /**
     * 停止消费
     */
    protected void stopConsume() {
        this.startConsume.set(false);
    }

    /**
     * 关闭
     */
    @PreDestroy
    public void shutdown() {
        if (!this.executorService.isShutdown()) {
            this.executorService.shutdown();
        }
        log.info("{} 队列{}已经关闭了", CoreUtil.getTraceId(), this.queueName);
    }

    /**
     * 消费任务
     *
     * @throws InterruptedException InterruptedException
     */
    private void consume() throws InterruptedException {
        //循环消费
        while (this.startConsume.get()) {
            //获得ID
            String id = getId();
            //获得的ID不为空,则开始处理任务
            if (StringUtils.isNotBlank(id)) {
                //获取信号量(如果满了,则阻塞等待)(控制并发执行数量)
                this.semaphore.acquire();
                //添加线程池任务
                this.executorService.execute(() -> {
                    try {
                        //定义锁定key
                        final String lockKey = "dbqueue:consume:" + id;
                        //锁定
                        if (iDbQueueLock.lock(lockKey)) {
                            //开始处理任务
                            try {
                                //获得任务详情
                                DbQueueEntity dbQueueEntity = dbQueueService.get(id);
                                //任务判空
                                if (dbQueueEntity != null) {
                                    //判断任务状态,只有待消费的才继续处理
                                    if (DbQueueConstant.DB_QUEUE_STATUS.PENDING_CONSUMPTION.value().equals(dbQueueEntity.getStatus())) {
                                        //定义消费消息
                                        String consumMsg = "消费成功";
                                        //定义消费状态flag
                                        boolean successFlag = false;
                                        //记录开始消费时间
                                        final long startConsumTime = System.currentTimeMillis();
                                        final Date consumeDate = new Date();
                                        //处理
                                        try {
                                            //添加处理计数器
                                            handleCounter.putIfAbsent(id, new AtomicInteger(0));
                                            //判断处理任务是否存在
                                            if (CoreConstant.applicationContext.containsBean(dbQueueEntity.getHanledBeanName())) {
                                                //获得处理任务
                                                IDbQueueHandle iDbQueueHandle = CoreConstant.applicationContext.getBean(dbQueueEntity.getHanledBeanName(), IDbQueueHandle.class);
                                                //处理任务
                                                iDbQueueHandle.handle(dbQueueEntity.getId(), dbQueueEntity.getBody());
                                                //更改消费状态为消费成功
                                                successFlag = true;
                                            } else {
                                                //抛出异常
                                                throw new SystemRuntimeException("任务处理器[" + dbQueueEntity.getHanledBeanName() + "]不存在");
                                            }
                                        } catch (Exception e) {
                                            //异常通知
                                            if (iExceptionAlert != null) {
                                                iExceptionAlert.alert(id, e);
                                            }
                                            //抛出错误
                                            throw e;
                                        } finally {
                                            //计算消费耗时
                                            final long onsumeCost = System.currentTimeMillis() - startConsumTime;
                                            //按消费状态处理
                                            if (successFlag) { //成功
                                                //任务处理完毕后状态回写
                                                dbQueueService.updateToConsumed(dbQueueEntity.getId(), consumeDate, onsumeCost, consumMsg);
                                                //清理处理计数器
                                                handleCounter.remove(id);
                                            } else { //失败
                                                //累加处理计数器
                                                int count = handleCounter.get(id).addAndGet(1);
                                                //比较处理次数
                                                if (count >= this.retryCount) {
                                                    //设置消费状态描述
                                                    consumMsg = "消费失败";
                                                    //任务处理完毕后状态回写
                                                    dbQueueService.updateToConsumed(dbQueueEntity.getId(), consumeDate, onsumeCost, consumMsg);
                                                    //清理处理计数器
                                                    handleCounter.remove(id);
                                                    //记录"死信队列"
                                                    if (iDataHis != null) {
                                                        iDataHis.save(id, this.dbQueueDeadLetterName, id, dbQueueService.get(id), this.queueName);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } finally {
                                //解锁
                                iDbQueueLock.unlock(lockKey);
                            }
                        }
                    } finally {
                        //释放信号量
                        this.semaphore.release();
                    }
                });
            }
        }
    }

    /**
     * 获得ID
     *
     * @return id
     * @throws InterruptedException InterruptedException
     */
    private String getId() throws InterruptedException {
        //取出消息
        String id = this.queue.poll();
        //如果为空
        if (StringUtils.isBlank(id)) {
            //从数据库中加载任务
            final int limit = 10;
            List<String> dbIds = dbQueueService.loadPendingConsumptionList(queueName, limit);
            //添加到队列中
            if (CollectionUtils.isNotEmpty(dbIds)) {
                for (String tempId : dbIds) {
                    boolean success = this.queue.offer(tempId);
                    if (!success) {
                        break;
                    }
                }
            }
            //清理处理计数器
            if (CollectionUtils.isNotEmpty(handleCounter.keySet())) {
                //没有要处理的任务了
                if (CollectionUtils.isEmpty(dbIds)) {
                    //清理计数器
                    handleCounter.clear();
                }
            }
            //阻塞等待,获取ID
            id = this.queue.take();
        }
        //返回
        return id;
    }
}
