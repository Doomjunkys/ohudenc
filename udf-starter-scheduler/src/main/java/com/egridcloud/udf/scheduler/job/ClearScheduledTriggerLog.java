/**
 * ClearScheduledTriggerLog.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.egridcloud.udf.scheduler.ITriggerLog;

/**
 * <p>
 * ClassName: ClearScheduledTriggerLog
 * </p>
 * <p>
 * Description: 清理计划任务执行日志
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2014年3月7日
 * </p>
 */
@DisallowConcurrentExecution
public class ClearScheduledTriggerLog extends AbstractBaseJob {

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    ITriggerLog triggerLog = this.getApplicationContext().getBean(ITriggerLog.class);
    if (triggerLog != null) {
      triggerLog.clearScheduledTriggerLog();
    }
  }

}
