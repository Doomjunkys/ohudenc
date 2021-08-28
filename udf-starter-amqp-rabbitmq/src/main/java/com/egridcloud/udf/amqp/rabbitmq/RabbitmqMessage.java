/**
 * Message.java
 * Created at 2016-09-19
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * 描述 : 消息
 *
 * 
 * 
 * @author wangkang
 *
 * 
 * 
 */
public class RabbitmqMessage<T> implements Serializable {

  /**
   * 
   * 描述 : id
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 
   * 描述 : 消息ID
   * 
   */
  private String id;

  /**
   * 发送者
   */
  private String sender;

  /**
   * 描述 : 生产者编号
   */
  private String producerCode;

  /**
   * 描述 : 交换机
   */
  private String exchange;

  /**
   * 描述 : 路由键
   */
  private String routingKey;

  /**
   * 描述 : 时间戳
   */
  private Long timestamp;

  /**
   * 描述 : 消息创建时间
   */
  private Date createDate;

  /**
   * 描述 : 消息发送时间
   */
  private Date sendDate;

  /**
   * 描述 : 消息头
   */
  private Map<String, String> header;

  /**
   * 
   * 描述 : 消息体
   * 
   */
  private T body; //NOSONAR

  /**
   * 描述 : 构造函数
   *
   */
  public RabbitmqMessage() {
    super();
  }

  /**
   * 描述 : 构造函数
   *
   * @param header 消息头
   * @param body 消息体
   */
  public RabbitmqMessage(Map<String, String> header, T body) {
    super();
    this.id = UUID.randomUUID().toString();
    this.createDate = new Date();
    this.timestamp = createDate.getTime();
    this.header = header;
    this.body = body;
  }

  /**
   * 描述 : 获取producerCode
   *
   * @return the producerCode
   */
  public String getProducerCode() {
    return producerCode;
  }

  /**
   * 描述 : 设置producerCode
   *
   * @param producerCode the producerCode to set
   */
  public void setProducerCode(String producerCode) {
    this.producerCode = producerCode;
  }

  /**
   * 描述 : 获取sender
   *
   * @return the sender
   */
  public String getSender() {
    return sender;
  }

  /**
   * 描述 : 设置sender
   *
   * @param sender the sender to set
   */
  public void setSender(String sender) {
    this.sender = sender;
  }

  /**
   * 描述 : 获取timestamp
   *
   * @return the timestamp
   */
  public Long getTimestamp() {
    return timestamp;
  }

  /**
   * 描述 : 设置timestamp
   *
   * @param timestamp the timestamp to set
   */
  public void setTimestamp(Long timestamp) {
    this.timestamp = timestamp;
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
   * 描述 : 获取sendDate
   *
   * @return the sendDate
   */
  public Date getSendDate() {
    return sendDate;
  }

  /**
   * 描述 : 设置sendDate
   *
   * @param sendDate the sendDate to set
   */
  public void setSendDate(Date sendDate) {
    this.sendDate = sendDate;
  }

  /**
   * 描述 : 获取createDate
   *
   * @return the createDate
   */
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * 描述 : 设置createDate
   *
   * @param createDate the createDate to set
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  /**
   * 描述 : 获取header
   *
   * @return the header
   */
  public Map<String, String> getHeader() {
    return header;
  }

  /**
   * 描述 : 设置header
   *
   * @param header the header to set
   */
  public void setHeader(Map<String, String> header) {
    this.header = header;
  }

  /**
   * 描述 : 获取id
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * 描述 : 设置id
   *
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * 描述 : 获取body
   *
   * @return the body
   */
  public T getBody() {
    return body;
  }

  /**
   * 描述 : 设置body
   *
   * @param body the body to set
   */
  public void setBody(T body) {
    this.body = body;
  }

}
