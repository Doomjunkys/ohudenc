/**
 * RabbitmqListenerLogAspect.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.amqp.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 描述 : RabbitmqListenerLogAspect
 *
 * @author wangkang
 */
@Aspect
@Component
@Order(100)
@Slf4j
public class RabbitmqListenerLogAspect extends RabbitmqListenerBaseAspect {

    /**
     * 描述 : 前置通知
     *
     * @param joinPoint joinPoint
     */
    @Before("listenerAspect()")
    public void before(JoinPoint joinPoint) {
        if (joinPoint.getTarget() instanceof IRabbitmqListener) {
            RabbitmqMessage<?> message = (RabbitmqMessage<?>) joinPoint.getArgs()[0];
            String queues = this.getQueues(joinPoint);
            log.info("{},{},receiveDate:{}", queues, message.getId(), new Date().getTime());
        }
    }

    /**
     * 描述 : 后置通知
     *
     * @param joinPoint joinPoint
     */
    @After("listenerAspect()")
    public void after(JoinPoint joinPoint) {
        if (joinPoint.getTarget() instanceof IRabbitmqListener) {
            RabbitmqMessage<?> message = (RabbitmqMessage<?>) joinPoint.getArgs()[0];
            String queues = this.getQueues(joinPoint);
            log.info("{},{},completeDate:{}", queues, message.getId(), new Date().getTime());
        }
    }

    /**
     * 异常通知
     *
     * @param joinPoint joinPoint
     * @param ex        ex
     */
    @AfterThrowing(pointcut = "listenerAspect()", throwing = "ex")
    public void fterThrowing(JoinPoint joinPoint, Exception ex) {
        if (joinPoint.getTarget() instanceof IRabbitmqListener) {
            RabbitmqMessage<?> message = (RabbitmqMessage<?>) joinPoint.getArgs()[0];
            String queues = this.getQueues(joinPoint);
            log.info("{},{},errorDate:{}", queues, message.getId(), new Date().getTime(), ex);
        }
    }

}
