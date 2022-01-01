/**
 * TriggerDetailListener.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.listener;

import org.quartz.JobExecutionContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.itkk.udf.scheduler.service.ListenerService;

/**
 * <p>
 * ClassName: TriggerListener
 * </p>
 * <p>
 * Description: 全局触发器监听
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2014年2月8日
 * </p>
 */
public class TriggerDetailListener implements TriggerListener {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(TriggerDetailListener.class);

  /**
   * 描述 : listenerService
   */
  @Autowired
  private ListenerService listenerService;

  @Override
  public String getName() {
    return "TriggerDetailListener";
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext context) { // 2
    try {
      this.listenerService.saveTriggerFired(context);
    } catch (SchedulerException e) {
      LOGGER.error("triggerFired error:", e);
    }
  }

  @Override
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) { // 3
    boolean vetoed = false;
    try {
      vetoed = this.listenerService.saveVetoJobExecution(context);
    } catch (SchedulerException e) {
      LOGGER.error("vetoJobExecution error:", e);
    }
    return vetoed;
  }

  @Override
  public void triggerMisfired(Trigger trigger) { // 1
    this.listenerService.saveTriggerMisfired(trigger);
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext context,
      CompletedExecutionInstruction triggerInstructionCode) { // 7
    try {
      this.listenerService.saveTriggerComplete(context, triggerInstructionCode);
    } catch (SchedulerException e) {
      LOGGER.error("triggerComplete error:", e);
    }
  }
}
