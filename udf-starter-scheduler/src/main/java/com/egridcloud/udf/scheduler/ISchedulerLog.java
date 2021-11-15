/**
 * ISchedulerLog.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

/**
 * 描述 : ISchedulerLog
 *
 * @author Administrator
 *
 */
public interface ISchedulerLog {

  /**
   * 描述 : 记录日志
   *
   * @param log log
   */
  public void save(String log);

  /**
   * 描述 : 清理日志
   *
   */
  public void clearScheduledLog();

}
