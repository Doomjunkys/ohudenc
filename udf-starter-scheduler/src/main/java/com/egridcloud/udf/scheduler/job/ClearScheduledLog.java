/**
 * ClearScheduledLog.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.egridcloud.udf.scheduler.ISchedulerLog;

/**
 * <p>
 * ClassName: ClearScheduledLog
 * </p>
 * <p>
 * Description: 清理计划任务日志
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2014年3月7日
 * </p>
 */
@DisallowConcurrentExecution
public class ClearScheduledLog extends AbstractBaseJob {

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    ISchedulerLog schedulerLog = this.getApplicationContext().getBean(ISchedulerLog.class);
    if (schedulerLog != null) {
      schedulerLog.clearScheduledLog();
    }
  }

}
