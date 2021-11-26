/**
 * SimpleTriggerMate.java
 * Created at 2017-06-02
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.mate;

import java.io.Serializable;

import org.quartz.SimpleTrigger;

/**
 * 描述 : SimpleTriggerMate.java
 *
 * @author Administrator
 *
 */
public class SimpleTriggerMate extends TriggerMate implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 重复次数
   */
  private Integer repeatCount = SimpleTrigger.REPEAT_INDEFINITELY;

  /**
   * 描述 : 执行间隔
   */
  private Long intervalInMilliseconds;

  /**
   * 描述 : 获取repeatCount
   *
   * @return the repeatCount
   */
  public Integer getRepeatCount() {
    return repeatCount;
  }

  /**
   * 描述 : 设置repeatCount
   *
   * @param repeatCount the repeatCount to set
   */
  public void setRepeatCount(Integer repeatCount) {
    this.repeatCount = repeatCount;
  }

  /**
   * 描述 : 获取intervalInMilliseconds
   *
   * @return the intervalInMilliseconds
   */
  public Long getIntervalInMilliseconds() {
    return intervalInMilliseconds;
  }

  /**
   * 描述 : 设置intervalInMilliseconds
   *
   * @param intervalInMilliseconds the intervalInMilliseconds to set
   */
  public void setIntervalInMilliseconds(Long intervalInMilliseconds) {
    this.intervalInMilliseconds = intervalInMilliseconds;
  }

}
