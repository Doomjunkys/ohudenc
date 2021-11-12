/**
 * AbstractBaseJob.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * 描述 : AbstractBaseJob
 *
 * @author Administrator
 *
 */
public abstract class AbstractBaseJob extends QuartzJobBean {

  /**
   * 描述 : spring上下文
   */
  private ApplicationContext applicationContext;

  @Override
  protected abstract void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException;

  public ApplicationContext getApplicationContext() {
    return this.applicationContext;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

}
