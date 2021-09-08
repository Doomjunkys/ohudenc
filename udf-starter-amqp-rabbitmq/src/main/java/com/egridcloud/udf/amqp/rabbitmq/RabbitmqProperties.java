/**
 * RabbitmqProperties.java
 * Created at 2017-05-23
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.io.Serializable;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.amqp.rabbitmq.mate.ConsumerMate;
import com.egridcloud.udf.amqp.rabbitmq.mate.ProducerMate;
import com.egridcloud.udf.amqp.rabbitmq.mate.PublisherMate;
import com.egridcloud.udf.amqp.rabbitmq.mate.SubscriberMate;

/**
 * 描述 : RabbitmqProperties
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.rabbitmq.properties")
public class RabbitmqProperties implements Serializable {

  /**
   * 描述 : 死信后缀
   */
  public static final String DEAD_LETTER_SUFFIX = ".dlx";

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 发布者 ( key:应用名称 )
   */
  private Map<String, PublisherMate> publisher;

  /**
   * 描述 : 订阅者 ( key:应用名称 )
   */
  private Map<String, SubscriberMate> subscriber;

  /**
   * 描述 : 生产者 ( key:编号 )
   */
  private Map<String, ProducerMate> producer;

  /**
   * 描述 : 消费者 ( key:编号 )
   */
  private Map<String, ConsumerMate> consumer;

  /**
   * 描述 : 获取publisher
   *
   * @return the publisher
   */
  public Map<String, PublisherMate> getPublisher() {
    return publisher;
  }

  /**
   * 描述 : 设置publisher
   *
   * @param publisher the publisher to set
   */
  public void setPublisher(Map<String, PublisherMate> publisher) {
    this.publisher = publisher;
  }

  /**
   * 描述 : 获取subscriber
   *
   * @return the subscriber
   */
  public Map<String, SubscriberMate> getSubscriber() {
    return subscriber;
  }

  /**
   * 描述 : 设置subscriber
   *
   * @param subscriber the subscriber to set
   */
  public void setSubscriber(Map<String, SubscriberMate> subscriber) {
    this.subscriber = subscriber;
  }

  /**
   * 描述 : 获取producer
   *
   * @return the producer
   */
  public Map<String, ProducerMate> getProducer() {
    return producer;
  }

  /**
   * 描述 : 设置producer
   *
   * @param producer the producer to set
   */
  public void setProducer(Map<String, ProducerMate> producer) {
    this.producer = producer;
  }

  /**
   * 描述 : 获取consumer
   *
   * @return the consumer
   */
  public Map<String, ConsumerMate> getConsumer() {
    return consumer;
  }

  /**
   * 描述 : 设置consumer
   *
   * @param consumer the consumer to set
   */
  public void setConsumer(Map<String, ConsumerMate> consumer) {
    this.consumer = consumer;
  }

}
