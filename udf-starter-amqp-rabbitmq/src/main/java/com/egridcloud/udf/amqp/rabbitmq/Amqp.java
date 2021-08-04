/**
 * Amqp.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.util.Date;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 描述 : Amqp
 *
 * @author wangkang
 *
 */
@Component
public class Amqp {

  /**
   * 描述 : amqpTemplate
   */
  @Autowired
  private AmqpTemplate amqpTemplate;

  /**
   * 描述 : Convert a Java object to an Amqp Message and send it to a specific exchange with a
   * specific routing key.
   * 
   * @param <T> 任意类型
   *
   * @param exchange the name of the exchange
   * @param routingKey the routing key
   * @param message a message to send
   */
  public <T> void convertAndSend(String exchange, String routingKey, Message<T> message) {
    this.buildMessage(message, exchange, routingKey);
    this.amqpTemplate.convertAndSend(exchange, routingKey, message);
  }

  /**
   * 描述 : 构建消息
   *
   * @param <T> 任意类型
   *
   * @param message message
   * @param exchange exchange
   * @param routingKey routingKey
   */
  private <T> void buildMessage(Message<T> message, String exchange, String routingKey) {
    message.setSendDate(new Date());
    message.setExchange(exchange);
    message.setRoutingKey(routingKey);
  }

}
