/**
 * Rabbitmq.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.amqp.rabbitmq.mate.ProducerMate;
import com.egridcloud.udf.amqp.rabbitmq.mate.PublisherMate;
import com.egridcloud.udf.core.exception.PermissionException;

/**
 * 描述 : Rabbitmq
 *
 * @author wangkang
 *
 */
@Component
public class Rabbitmq {

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
   * 描述 : amqpTemplate
   */
  @Autowired
  private AmqpTemplate amqpTemplate;

  /**
   * 描述 : Convert a Java object to an Amqp Message and send it to a specific exchange with a
   * specific routing key.
   *
   * @param producerCode producerCode
   * @param message a message to send
   * @param <T> 任意类型
   */
  public <T> void convertAndSend(String producerCode, RabbitmqMessage<T> message) {
    //校验
    verification(producerCode);
    //获得交换机和路由键
    String exchange = getExchange(producerCode);
    String routingKey = getRoutingKey(producerCode);
    //构造消息
    this.buildMessage(message, producerCode, exchange, routingKey);
    //发送
    this.amqpTemplate.convertAndSend(exchange, routingKey, message);
  }

  /**
   * 描述 : 获得交换机
   *
   * @param producerCode producerCode
   * @return Exchange
   */
  private String getExchange(String producerCode) {
    return rabbitmqProperties.getProducer().get(producerCode).getExchange();
  }

  /**
   * 描述 : 获得路由键
   *
   * @param producerCode producerCode
   * @return RoutingKey
   */
  private String getRoutingKey(String producerCode) {
    return rabbitmqProperties.getProducer().get(producerCode).getRoutingKey();
  }

  /**
   * 描述 : 校验
   *
   * @param producerCode producerCode
   */
  private void verification(String producerCode) {
    PublisherMate publisherMate = rabbitmqProperties.getPublisher().get(springApplicationName);
    ProducerMate producerMate = rabbitmqProperties.getProducer().get(producerCode);
    if (publisherMate != null) {
      if (publisherMate.getPurview().indexOf(producerCode) == -1) {
        throw new PermissionException("no access to this producerCode  : " + producerCode);
      }
    } else {
      throw new PermissionException("Publisher:" + springApplicationName + " not definition");
    }
    if (producerMate == null) {
      throw new PermissionException("Producer:" + producerCode + " not definition");
    }
  }

  /**
   * 描述 : 构建消息
   *
   * @param <T> 任意类型
   *
   * @param message message
   * @param producerCode producerCode
   * @param exchange exchange
   * @param routingKey routingKey
   */
  private <T> void buildMessage(RabbitmqMessage<T> message, String producerCode, String exchange,
      String routingKey) {
    message.setProducerCode(producerCode);
    message.setSender(springApplicationName);
    message.setExchange(exchange);
    message.setRoutingKey(routingKey);
    message.setSendDate(new Date());
  }

}
