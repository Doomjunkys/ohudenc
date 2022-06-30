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

import java.util.Date;

/**
 * IdWorkInit
 */
@Component
@Slf4j
public class IdWorkerInit {

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
    private static final long JOB_RUN_TIME = 20 * 1000;

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
     * 初始化
     */
    public synchronized void init() {
        //开始处理
        boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            try {
                //实例化解析器
                StringRedisSerializer serializer = new StringRedisSerializer();
                //锁定标记
                boolean lock = false;
                //循环
                for (int i = 0; i <= MAX_COUNT; i++) { //datacenterId
                    for (int j = 0; j <= MAX_COUNT; j++) { //workerId
                        //生成key
                        String key = cacheRedisProperties.getPrefix().concat(SPLIT).concat(CACHE_NAME).concat(SPLIT).concat(i + "").concat(SPLIT).concat(j + "");
                        //创建锁
                        lock = connection.setNX(serializer.serialize(key), serializer.serialize(String.valueOf(new Date().getTime())));
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
        if (this.workerId != null && this.datacenterId != null & this.idWorker != null) {
            //开始处理
            boolean result = redisTemplate.execute((RedisCallback<Boolean>) connection -> {
                try {
                    //实例化解析器
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    //生成key
                    String key = cacheRedisProperties.getPrefix().concat(SPLIT).concat(CACHE_NAME).concat(SPLIT).concat(this.datacenterId + "").concat(SPLIT).concat(this.workerId + "");
                    //如果key不存在就创建(方式特殊情况,缓存丢失掉)
                    connection.setNX(serializer.serialize(key), serializer.serialize(String.valueOf(new Date().getTime())));
                    //设置超时时间
                    return connection.expire(serializer.serialize(key), EXPIRATION);
                } finally {
                    connection.close();
                }
            });
            //日志输出
            log.info("refresh IdWorker success = {} , {} , {}", result, this.datacenterId, this.workerId);
        }
    }
}
