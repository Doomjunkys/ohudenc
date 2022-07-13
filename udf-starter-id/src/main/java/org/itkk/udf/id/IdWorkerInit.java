package org.itkk.udf.id;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.cache.redis.CacheRedisProperties;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.UUID;

/**
 * IdWorkInit
 */
@Component
@Slf4j
public class IdWorkerInit {

    /**
     * MAX_SEQUENCE
     */
    public static final int MAX_SEQUENCE = 4096;

    /**
     * 机器ID和数据中心ID的最大值
     */
    private static final int MAX_COUNT = 31;

    /**
     * 缓存名称
     */
    private static final String CACHE_NAME = "id";

    /**
     * 分隔符
     */
    private static final String SPLIT = "_";

    /**
     * key的过期时间
     */
    private static final long EXPIRATION = 30;

    /**
     * job执行时间
     */
    private static final long JOB_RUN_TIME = 20l * 1000l;

    /**
     * 机器ID( 0 - 31 )
     */
    private Integer workerId;

    /**
     * 描述 : 数据中心ID( 0 - 31 )
     */
    private Integer datacenterId;

    /**
     * ID生成器
     */
    private IdWorker idWorker;

    /**
     * 缓存值
     */
    private String cacheValue;

    /**
     * serializer
     */
    private StringRedisSerializer serializer = new StringRedisSerializer();

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * cacheRedisProperties
     */
    @Autowired
    private CacheRedisProperties cacheRedisProperties;

    /**
     * 返回ID生成器
     *
     * @return IdWorker
     */
    public IdWorker get() {
        return this.idWorker;
    }

    /**
     * 初始化(默认实例化)
     */
    @PostConstruct
    public synchronized void init() {
        //开始处理
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            try {
                //锁定标记
                boolean lock = false;
                //循环
                for (int i = 0; i <= MAX_COUNT; i++) { //datacenterId
                    for (int j = 0; j <= MAX_COUNT; j++) { //workerId
                        //生成一个uuid
                        cacheValue = UUID.randomUUID().toString();
                        //生成key
                        String key = cacheRedisProperties.getPrefix().concat(SPLIT).concat(CACHE_NAME).concat(SPLIT).concat(Integer.toString(i)).concat(SPLIT).concat(Integer.toString(j));
                        //创建锁
                        lock = connection.setNX(serializer.serialize(key), serializer.serialize(cacheValue));
                        //获得锁成功
                        if (lock) {
                            //设置超时时间
                            connection.expire(serializer.serialize(key), EXPIRATION);
                            //设置数据中心ID
                            this.datacenterId = i;
                            //设置机器ID
                            this.workerId = j;
                            //实例化ID生成器
                            this.idWorker = new IdWorker(this.workerId, this.datacenterId);
                            //跳出
                            break;
                        }
                    }
                    //判断是否跳出
                    if (lock) {
                        break;
                    }
                }
                //如果循环完毕还没有实例化完成ID生成器,则抛出以上
                if (this.idWorker == null) {
                    throw new SystemRuntimeException("实例化IdWork失败");
                }
                return lock;
            } finally {
                connection.close();
            }
        });
        //日志输出
        log.info("init IdWorker success = {} , {} , {}", result, this.datacenterId, this.workerId);
    }

    /**
     * 定时刷新过期时间
     */
    @Scheduled(fixedRate = JOB_RUN_TIME)
    public void refresh() {
        //id生成器实例化成功的情况下执行
        if (this.workerId != null && this.datacenterId != null && this.idWorker != null) {
            //开始处理
            boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                try {
                    //生成key
                    String key = cacheRedisProperties.getPrefix().concat(SPLIT).concat(CACHE_NAME).concat(SPLIT).concat(Integer.toString(this.datacenterId)).concat(SPLIT).concat(Integer.toString(this.workerId));
                    //如果key不存在就创建(防止特殊情况,缓存丢失掉)
                    connection.setNX(serializer.serialize(key), serializer.serialize(cacheValue));
                    //获得值(用于确定锁是否是自己创建的)
                    String currentCacheVlue = serializer.deserialize(connection.get(serializer.serialize(key)));
                    //比较(如果值是一样,则代表自己拥有锁,如果值不一样,则代表锁已经被其他进程获取)
                    if (cacheValue.equals(currentCacheVlue)) {
                        //设置超时时间
                        return connection.expire(serializer.serialize(key), EXPIRATION);
                    } else {
                        //重新初始化
                        this.init();
                        //返回
                        return true;
                    }
                } finally {
                    connection.close();
                }
            });
            //日志输出
            log.info("refresh IdWorker success = {} , {} , {}", result, this.datacenterId, this.workerId);
        }
    }
}
