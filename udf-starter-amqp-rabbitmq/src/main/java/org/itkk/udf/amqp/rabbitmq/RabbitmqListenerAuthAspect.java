/**
 * RabbitmqListenerAuthAspect.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.amqp.rabbitmq;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.itkk.udf.amqp.rabbitmq.meta.ConsumerMeta;
import org.itkk.udf.amqp.rabbitmq.meta.SubscriberMeta;
import org.itkk.udf.core.exception.PermissionException;

/**
 * 描述 : RabbitmqListenerAuthAspect
 *
 * @author wangkang
 *
 */
@Aspect
@Component
@Order(99)
public class RabbitmqListenerAuthAspect extends RabbitmqListenerBaseAspect {

  /**
   * 描述 : 应用名称
   */
  @Value("${spring.application.name}")
  private String springApplicationName;

  /**
   * 描述 : rabbitmqProperties
   */
  @Autowired
  private RabbitmqProperties rabbitmqProperties;

  /**
   * 描述 : 前置通知
   *
   * @param joinPoint joinPoint
   * @throws ClassNotFoundException
   */
  @Before("listenerAspect()")
  public void before(JoinPoint joinPoint) {
    if (joinPoint.getTarget() instanceof IRabbitmqListener) {
      //获得当前类的consumerCode
      String consumerCode = getConsumerCode(joinPoint);
      //获得消息
      RabbitmqMessage<?> message = (RabbitmqMessage<?>) joinPoint.getArgs()[0];
      //验证
      verification(consumerCode, message);
    }
  }

  /**
   * 描述 : 校验
   *
   * @param consumerCode consumerCode
   * @param message message
   */
  private void verification(String consumerCode, RabbitmqMessage<?> message) {
    //获得subscriberMeta
    SubscriberMeta subscriberMeta = rabbitmqProperties.getSubscriber().get(springApplicationName);
    //获得consumerMeta
    ConsumerMeta consumerMeta = rabbitmqProperties.getConsumer().get(consumerCode);
    //判断订阅者是否有此consumerCode的权限
    if (subscriberMeta != null) {
      if (subscriberMeta.getPurview().indexOf(consumerCode) == -1) {
        throw new PermissionException("no access to this consumerCode  : " + consumerCode);
      }
    } else {
      throw new PermissionException("Subscriber:" + springApplicationName + " not definition");
    }
    //判断此consumerCode的权限
    if (consumerMeta != null) {
      if (message.getProducerCode().equals(consumerMeta.getProducer())) {
        throw new PermissionException(
            "no access to this ProducerCode  : " + message.getProducerCode());
      }
    } else {
      throw new PermissionException("Consumer:" + consumerCode + " not definition");
    }
  }

}
