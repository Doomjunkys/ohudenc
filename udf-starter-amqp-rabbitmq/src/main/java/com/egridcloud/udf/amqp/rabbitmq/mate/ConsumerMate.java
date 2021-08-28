/**
 * ConsumerMate.java
 * Created at 2017-05-23
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq.mate;

/**
 * 描述 : ConsumerMate
 *
 * @author Administrator
 *
 */
public class ConsumerMate {

  /**
   * 描述 : 应用名称
   */
  private String owner;

  /**
   * 描述 : 生产者
   */
  private String producer;

  /**
   * 描述 : 描述
   */
  private String description;

  /**
   * 描述 : 获取owner
   *
   * @return the owner
   */
  public String getOwner() {
    return owner;
  }

  /**
   * 描述 : 设置owner
   *
   * @param owner the owner to set
   */
  public void setOwner(String owner) {
    this.owner = owner;
  }

  /**
   * 描述 : 获取producer
   *
   * @return the producer
   */
  public String getProducer() {
    return producer;
  }

  /**
   * 描述 : 设置producer
   *
   * @param producer the producer to set
   */
  public void setProducer(String producer) {
    this.producer = producer;
  }

  /**
   * 描述 : 获取description
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * 描述 : 设置description
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

}
