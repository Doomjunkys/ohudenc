package org.itkk.udf.starter.queue.db.job;

import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.job.IJobHandle;
import org.itkk.udf.starter.queue.db.IDbQueueLock;
import org.itkk.udf.starter.queue.db.service.DbQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClearDbQueueJob implements IJobHandle {
    /**
     * iDbQueueRepository
     */
    @Autowired
    private IDbQueueLock iDbQueueLock;

    /**
     * dbQueueService
     */
    @Autowired
    private DbQueueService dbQueueService;

    @Override
    @Scheduled(cron = "0 0/10 * * * ?")
    public void execution() {
        //开始时间
        long startTime = System.currentTimeMillis();
        //定义key
        final String key = "job:clearConsumedDbQueueMessage";
        //定义过期时间(五分钟)
        final long expire = 5 * 60 * 1000;
        //定义job的traceId
        final String traceId = CoreUtil.getTraceId();
        //悲观锁-锁定任务
        if (iDbQueueLock.lock(key)) {
            //日志输出
            log.info("{} 清理已消费的数据库队列消息(lock success)", traceId);
            //开始处理
            try {
                dbQueueService.clearDbQueue();
            } finally {
                //删除key
                iDbQueueLock.unlock(key);
                //日志输出
                log.info("{} 清理已消费的数据库队列消息(unlock success)(耗时:{}毫秒)", traceId, (System.currentTimeMillis() - startTime));
            }
        } else {
            //日志输出
            log.info("{} 清理已消费的数据库队列消息(lock fail)", traceId);
        }
    }
}
