/**
 * SchedulerProperties.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import org.itkk.udf.scheduler.meta.CronTriggerMeta;
import org.itkk.udf.scheduler.meta.JobDetailMeta;
import org.itkk.udf.scheduler.meta.SimpleTriggerMeta;

/**
 * 描述 : SchedulerProperties
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "org.itkk.scheduler.properties")
public class SchedulerProperties {

  /**
   * 描述 : 作业组别 ( key : 组别代码 , value : 组别描述 )
   */
  private Map<String, String> jobGroup;

  /**
   * 描述 : 触发器组别 ( key : 组别代码 , value : 组别描述 )
   */
  private Map<String, String> triggerGroup;

  /**
   * 描述 : 作业 ( kety : 作业代码[jobCode] )
   */
  private Map<String, JobDetailMeta> jobDetail;

  /**
   * 描述 : 简单触发器 ( key: 触发器代码[simpleTriggerCode] )
   */
  private Map<String, SimpleTriggerMeta> simpleTrigger;

  /**
   * 描述 : cron触发器 (key : 触发器代码[cronTriggerCode])
   */
  private Map<String, CronTriggerMeta> cronTrigger;

  /**
   * 描述 : 获取cronTrigger
   *
   * @return the cronTrigger
   */
  public Map<String, CronTriggerMeta> getCronTrigger() {
    return cronTrigger;
  }

  /**
   * 描述 : 设置cronTrigger
   *
   * @param cronTrigger the cronTrigger to set
   */
  public void setCronTrigger(Map<String, CronTriggerMeta> cronTrigger) {
    this.cronTrigger = cronTrigger;
  }

  /**
   * 描述 : 获取simpleTrigger
   *
   * @return the simpleTrigger
   */
  public Map<String, SimpleTriggerMeta> getSimpleTrigger() {
    return simpleTrigger;
  }

  /**
   * 描述 : 设置simpleTrigger
   *
   * @param simpleTrigger the simpleTrigger to set
   */
  public void setSimpleTrigger(Map<String, SimpleTriggerMeta> simpleTrigger) {
    this.simpleTrigger = simpleTrigger;
  }

  /**
   * 描述 : 获取jobGroup
   *
   * @return the jobGroup
   */
  public Map<String, String> getJobGroup() {
    return jobGroup;
  }

  /**
   * 描述 : 设置jobGroup
   *
   * @param jobGroup the jobGroup to set
   */
  public void setJobGroup(Map<String, String> jobGroup) {
    this.jobGroup = jobGroup;
  }

  /**
   * 描述 : 获取triggerGroup
   *
   * @return the triggerGroup
   */
  public Map<String, String> getTriggerGroup() {
    return triggerGroup;
  }

  /**
   * 描述 : 设置triggerGroup
   *
   * @param triggerGroup the triggerGroup to set
   */
  public void setTriggerGroup(Map<String, String> triggerGroup) {
    this.triggerGroup = triggerGroup;
  }

  /**
   * 描述 : 获取jobDetail
   *
   * @return the jobDetail
   */
  public Map<String, JobDetailMeta> getJobDetail() {
    return jobDetail;
  }

  /**
   * 描述 : 设置jobDetail
   *
   * @param jobDetail the jobDetail to set
   */
  public void setJobDetail(Map<String, JobDetailMeta> jobDetail) {
    this.jobDetail = jobDetail;
  }

}
