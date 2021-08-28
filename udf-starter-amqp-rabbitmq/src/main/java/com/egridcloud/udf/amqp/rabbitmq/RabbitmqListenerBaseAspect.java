/**
 * RabbitmqListenerAspect.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import com.egridcloud.udf.core.exception.SystemRuntimeException;

/**
 * 描述 : RabbitmqListenerBaseAspect
 *
 * @author wangkang
 *
 */
public class RabbitmqListenerBaseAspect {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RabbitmqListenerBaseAspect.class);

  /**
   * 描述 : listenerAspect
   *
   */
  @Pointcut("execution(void com.egridcloud..*.process(..)) "
      + "and @annotation(org.springframework.amqp.rabbit.annotation.RabbitListener) "
      + "and this(com.egridcloud.udf.amqp.rabbitmq.IRabbitmqListener)")
  protected void listenerAspect() {
    LOGGER.debug("RabbitmqListenerBaseAspect.listenerAspect");
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

  /**
   * 描述 : 获得consumerCode
   *
   * @param joinPoint joinPoint
   * @return consumerCode
   */
  protected String getConsumerCode(JoinPoint joinPoint) { //NOSONAR
    String methodName = joinPoint.getSignature().getName();
    Method[] methods = joinPoint.getTarget().getClass().getMethods();
    for (Method method : methods) { //循环方法
      if (method.getName().equals(methodName)) { //定位方法
        if (method.isAnnotationPresent(Consumer.class)) { //判断是否包含Consumer注解
          for (Annotation anno : method.getDeclaredAnnotations()) { //循环注解
            if (anno.annotationType().equals(Consumer.class)) { //定位注解
              Consumer consumer = (Consumer) anno; //获得注解
              if (StringUtils.isNotBlank(consumer.value())) { //判断值
                return consumer.value(); //返回
              } else {
                throw new SystemRuntimeException("@Consumer value is null");
              }
            }
          }
        } else {
          throw new SystemRuntimeException(
              "You must use the @Consumer annotation to mark the IRabbitmqListener method");
        }
      }
    }
    return null;
  }

}
