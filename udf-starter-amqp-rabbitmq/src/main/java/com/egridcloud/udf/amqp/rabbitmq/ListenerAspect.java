/**
 * ListenerAspect.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.core.exception.SystemRuntimeException;

/**
 * 描述 : ListenerAspect
 *
 * @author wangkang
 *
 */
@Aspect
@Component
public class ListenerAspect {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ListenerAspect.class);

  /**
   * 描述 : listenerAspect
   *
   */
  @Pointcut("execution(void com.egridcloud..*.process(..)) "
      + "and @annotation(org.springframework.amqp.rabbit.annotation.RabbitListener) "
      + "and this(com.egridcloud.udf.amqp.rabbitmq.IListener)")
  public void listenerAspect() {
    LOGGER.debug("ListenerAspect.listenerAspect");
  }

  /**
   * 描述 : 获得对列名称
   *
   * @param joinPoint joinPoint
   * @return 对列名称
   */
  @SuppressWarnings("rawtypes")
  private String getQueues(JoinPoint joinPoint) {
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

  /**
   * 描述 : 前置通知
   *
   * @param joinPoint joinPoint
   * @throws ClassNotFoundException
   */
  @Before("listenerAspect()")
  public void before(JoinPoint joinPoint) {
    if (joinPoint.getTarget() instanceof IListener) {
      Message<?> message = (Message<?>) joinPoint.getArgs()[0];
      String queues = this.getQueues(joinPoint);
      LOGGER.debug("{},{},receiveDate:{}", queues, message.getId(), new Date().getTime());
    }
  }

  /**
   * 描述 : 后置通知
   *
   * @param joinPoint joinPoint
   */
  @After("listenerAspect()")
  public void after(JoinPoint joinPoint) {
    if (joinPoint.getTarget() instanceof IListener) {
      Message<?> message = (Message<?>) joinPoint.getArgs()[0];
      String queues = this.getQueues(joinPoint);
      LOGGER.debug("{},{},completeDate:{}", queues, message.getId(), new Date().getTime());
    }
  }

  /**
   * 异常通知
   * 
   * @param joinPoint joinPoint
   * @param ex ex
   */
  @AfterThrowing(pointcut = "listenerAspect()", throwing = "ex")
  public void fterThrowing(JoinPoint joinPoint, Exception ex) {
    if (joinPoint.getTarget() instanceof IListener) {
      Message<?> message = (Message<?>) joinPoint.getArgs()[0];
      String queues = this.getQueues(joinPoint);
      LOGGER.debug("{},{},errorDate:{}", queues, message.getId(), new Date().getTime(), ex);
    }
  }

}
