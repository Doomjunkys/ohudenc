/**
 * IListener.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.amqp;

/**
 * 描述 : IListener
 *
 * @author wangkang
 *
 */
public interface IListener<T> { //NOSONAR

  /**
   * 描述 : 处理消息
   *
   * @param message 消息
   */
  public void process(Message<T> message);

}
