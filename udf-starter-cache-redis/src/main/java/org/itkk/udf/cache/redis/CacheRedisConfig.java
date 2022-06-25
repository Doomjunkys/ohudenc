package org.itkk.udf.cache.redis;

import org.itkk.udf.core.ApplicationConfig;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * CacheRedisConfig
 */
@EnableCaching
@Configuration
public class CacheRedisConfig {

    /**
     * DEFAULT_EXPIRATION
     */
    public static final long DEFAULT_EXPIRATION = 60;

    /**
     * cacheManager
     *
     * @param applicationConfig    applicationConfig
     * @param cacheRedisProperties cacheRedisProperties
     * @param redisTemplate        redisTemplate
     * @return CacheManager
     */
    @Bean
    public CacheManager cacheManager(ApplicationConfig applicationConfig, CacheRedisProperties cacheRedisProperties, RedisTemplate<Object, Object> redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
        cacheManager.setTransactionAware(true);
        cacheManager.setUsePrefix(cacheRedisProperties.getUsePrefix());
        cacheManager.setCachePrefix(new DefRedisCachePrefix(cacheRedisProperties.getPrefix()));
        cacheManager.setDefaultExpiration(cacheRedisProperties.getDefaultExpiration());
        cacheManager.setExpires(cacheRedisProperties.getNames());
        return cacheManager;
    }

    /**
     * redisTemplate
     *
     * @param factory factory
     * @return RedisTemplate
     */
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
