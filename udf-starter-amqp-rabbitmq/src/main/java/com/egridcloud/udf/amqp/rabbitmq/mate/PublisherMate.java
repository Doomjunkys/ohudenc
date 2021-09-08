/**
 * PublisherMate.java
 * Created at 2017-05-23
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq.mate;

import java.io.Serializable;

/**
 * 描述 : PublisherMate
 *
 * @author Administrator
 *
 */
public class PublisherMate implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 权限列表
   */
  private String purview;

  /**
   * 描述 : 描述
   */
  private String description;

  /**
   * 描述 : 获取purview
   *
   * @return the purview
   */
  public String getPurview() {
    return purview;
  }

  /**
   * 描述 : 设置purview
   *
   * @param purview the purview to set
   */
  public void setPurview(String purview) {
    this.purview = purview;
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
