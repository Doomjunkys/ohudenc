/**
 * ProducerMeta.java
 * Created at 2017-05-23
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq.meta;

import java.io.Serializable;

/**
 * 描述 : ProducerMeta
 *
 * @author Administrator
 *
 */
public class ProducerMeta implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 应用名称
   */
  private String owner;

  /**
   * 描述 : 交换机
   */
  private String exchange;

  /**
   * 描述 : 路由键
   */
  private String routingKey;

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
   * 描述 : 获取exchange
   *
   * @return the exchange
   */
  public String getExchange() {
    return exchange;
  }

  /**
   * 描述 : 设置exchange
   *
   * @param exchange the exchange to set
   */
  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  /**
   * 描述 : 获取routingKey
   *
   * @return the routingKey
   */
  public String getRoutingKey() {
    return routingKey;
  }

  /**
   * 描述 : 设置routingKey
   *
   * @param routingKey the routingKey to set
   */
  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
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
