package org.itkk.udf.cache.redis.lock;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.cache.redis.CacheRedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * RedisBasedDistributedLock
 */
@Component
@Slf4j
public class RedisBasedDistributedLock {

    /**
     * DISTRIBUTED_LOCK_KEY_PREFIX
     */
    private static final String DISTRIBUTED_LOCK_KEY_PREFIX = ":DISTRIBUTED_LOCK:";

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
     * lock
     *
     * @param name      name
     * @param timeout   timeout
     * @param timeUnit  timeUnit
     * @param lockValue lockValue
     * @param <T>       <T>
     * @return boolean
     */
    public <T> boolean lock(String name, long timeout, TimeUnit timeUnit, T lockValue) {
        //获得key
        String key = cacheRedisProperties.getPrefix() + DISTRIBUTED_LOCK_KEY_PREFIX + name;
        //检查是否是死锁
        if (redisTemplate.hasKey(key)) {
            //获得缓存过期时间
            long expiration = redisTemplate.getExpire(key);
            //如果等于-1,则代表死锁
            if (expiration == -1) {
                //删除缓存
                redisTemplate.delete(key);
            }
        }
        //创建锁
        boolean lock = redisTemplate.opsForValue().setIfAbsent(key, lockValue);
        //锁成功
        if (lock) {
            //设置超时时间
            redisTemplate.expire(key, timeout, timeUnit);
        }
        //日志输出
        log.info("RedisBasedDistributedLock : lock key : {} : {}", key, lock);
        //返回
        return lock;
    }

    /**
     * unlock
     *
     * @param name name
     */
    public void unlock(String name) {
        //获得key
        String key = cacheRedisProperties.getPrefix() + DISTRIBUTED_LOCK_KEY_PREFIX + name;
        //删除缓存
        redisTemplate.delete(key);
        //日志输出
        log.info("RedisBasedDistributedLock : unlock key : {}", key);
    }

}
