/**
 * MailProperties.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.mail;

import java.io.Serializable;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述 : MailProperties
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "org.itkk.mail.properties")
public class MailProperties implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 发送地址
   */
  private String from;

  /**
   * 描述 : 获取from
   *
   * @return the from
   */
  public String getFrom() {
    return from;
  }

  /**
   * 描述 : 设置from
   *
   * @param from the from to set
   */
  public void setFrom(String from) {
    this.from = from;
  }

}
