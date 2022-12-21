/**
 * RabbitmqListenerAspect.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file.aliyun.oss.api.download;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.itkk.udf.cache.redis.CacheRedisProperties;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.itkk.udf.file.aliyun.oss.api.OssWarpper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * 描述 : DownLoadProcessAspect
 *
 * @author wangkang
 */
@Aspect
@Component
@Order(101)
@Slf4j
public class DownLoadProcessAspect {

    /**
     * DOWNLOAD_CACHE_PREFIX
     */
    public static final String DOWNLOAD_CACHE_PREFIX = ":DOWNLOAD_CACHE:";

    /**
     * DOWNLOAD_CACHE_EXPIRATION
     */
    public static final long DOWNLOAD_CACHE_EXPIRATION = 10;

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
     * ossWarpper
     */
    @Autowired
    private OssWarpper ossWarpper;

    /**
     * around
     *
     * @param proceedingJoinPoint proceedingJoinPoint
     * @return Object
     */
    @Around("this(org.itkk.udf.file.aliyun.oss.api.download.IDownLoadProcess)" +
            " && @annotation(org.itkk.udf.file.aliyun.oss.api.download.DownLoad)" +
            " && args(org.itkk.udf.file.aliyun.oss.api.download.DownLoadParam)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) {
        //定义返回值
        Object result = null;
        //判断类型
        if (proceedingJoinPoint.getTarget() instanceof IDownLoadProcess) {
            //获得参数
            DownLoadParam<?> param = (DownLoadParam<?>) proceedingJoinPoint.getArgs()[0];
            //获得key
            String key = cacheRedisProperties.getPrefix() + DOWNLOAD_CACHE_PREFIX + param.getId();
            //构造对象
            DownloadInfo info = new DownloadInfo();
            info.setId(param.getId());
            try {
                //更新缓存
                info.setStatus(DownConstant.DOWNLOAD_PROCESS_STATUS.STATUS_2.value());
                redisTemplate.opsForValue().set(key, info, DOWNLOAD_CACHE_EXPIRATION, TimeUnit.MINUTES);
                //执行
                result = proceedingJoinPoint.proceed();
                //获得本地文件
                File file = (File) result;
                //上传文件到阿里云OSS
                String objectKey = ossWarpper.uploadFile(param.getOssCode(), file);
                //删除本地文件
                Files.delete(file.toPath());
                //更新缓存
                info.setStatus(DownConstant.DOWNLOAD_PROCESS_STATUS.STATUS_3.value());
                info.setObjectKey(objectKey);
                redisTemplate.opsForValue().set(key, info, DOWNLOAD_CACHE_EXPIRATION, TimeUnit.MINUTES);
            } catch (Throwable e) {
                //更新缓存
                info.setStatus(DownConstant.DOWNLOAD_PROCESS_STATUS.STATUS_4.value());
                info.setErrorMsg(e.getMessage());
                redisTemplate.opsForValue().set(key, info, DOWNLOAD_CACHE_EXPIRATION, TimeUnit.MINUTES);
                //抛出异常
                throw new SystemRuntimeException(e);
            }
        }
        //返回
        return result;
    }

}
