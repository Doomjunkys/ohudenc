/**
 * RabbitmqListenerAspect.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.amqp.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 描述 : RabbitmqListenerBaseAspect
 *
 * @author wangkang
 */
@Slf4j
public class RabbitmqListenerBaseAspect {

    /**
     * 描述 : listenerAspect
     */
    @Pointcut("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener) && this(org.itkk.udf.amqp.rabbitmq.IRabbitmqListener)")
    protected void listenerAspect() {
        log.debug("RabbitmqListenerBaseAspect.listenerAspect");
    }

    /**
     * 描述 : 获得对列名称
     *
     * @param joinPoint joinPoint
     * @return 对列名称
     */
    @SuppressWarnings("rawtypes")
    protected String getQueues(JoinPoint joinPoint) {
        try {
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) { //NOSONAR
                        return Arrays.toString(method.getAnnotation(RabbitListener.class).queues());
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            throw new SystemRuntimeException(e);
        }
        return null;
    }

}
