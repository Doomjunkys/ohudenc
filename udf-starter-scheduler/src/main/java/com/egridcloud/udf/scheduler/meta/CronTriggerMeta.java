/**
 * CronTriggerMeta.java
 * Created at 2017-06-02
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.scheduler.meta;

import java.io.Serializable;

/**
 * 描述 : CronTriggerMeta.java
 *
 * @author Administrator
 *
 */
public class CronTriggerMeta extends TriggerMeta implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : cron表达式
   */
  private String cron;

  /**
   * 描述 : 时区ID
   */
  private String timeZoneId;

  /**
   * 描述 : 获取cron
   *
   * @return the cron
   */
  public String getCron() {
    return cron;
  }

  /**
   * 描述 : 设置cron
   *
   * @param cron the cron to set
   */
  public void setCron(String cron) {
    this.cron = cron;
  }

  /**
   * 描述 : 获取timeZoneId
   *
   * @return the timeZoneId
   */
  public String getTimeZoneId() {
    return timeZoneId;
  }

  /**
   * 描述 : 设置timeZoneId
   *
   * @param timeZoneId the timeZoneId to set
   */
  public void setTimeZoneId(String timeZoneId) {
    this.timeZoneId = timeZoneId;
  }

}
