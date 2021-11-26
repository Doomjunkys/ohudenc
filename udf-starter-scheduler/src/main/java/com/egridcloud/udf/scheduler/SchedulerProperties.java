/**
 * SchedulerProperties.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.scheduler.mate.CronTriggerMate;
import com.egridcloud.udf.scheduler.mate.JobDetailMate;
import com.egridcloud.udf.scheduler.mate.SimpleTriggerMate;

/**
 * 描述 : SchedulerProperties
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.scheduler.properties")
public class SchedulerProperties {

  /**
   * 描述 : 组别 ( key : 组别代码 , value : 组别描述 )
   */
  private Map<String, String> group;

  /**
   * 描述 : 作业 ( kety : 作业代码[jobCode] )
   */
  private Map<String, JobDetailMate> jobDetail;

  /**
   * 描述 : 简单触发器 ( key: 触发器代码[simpleTriggerCode] )
   */
  private Map<String, SimpleTriggerMate> simpleTrigger;

  /**
   * 描述 : cron触发器 (key : 触发器代码[cronTriggerCode])
   */
  private Map<String, CronTriggerMate> cronTrigger;

  /**
   * 描述 : 获取cronTrigger
   *
   * @return the cronTrigger
   */
  public Map<String, CronTriggerMate> getCronTrigger() {
    return cronTrigger;
  }

  /**
   * 描述 : 设置cronTrigger
   *
   * @param cronTrigger the cronTrigger to set
   */
  public void setCronTrigger(Map<String, CronTriggerMate> cronTrigger) {
    this.cronTrigger = cronTrigger;
  }

  /**
   * 描述 : 获取simpleTrigger
   *
   * @return the simpleTrigger
   */
  public Map<String, SimpleTriggerMate> getSimpleTrigger() {
    return simpleTrigger;
  }

  /**
   * 描述 : 设置simpleTrigger
   *
   * @param simpleTrigger the simpleTrigger to set
   */
  public void setSimpleTrigger(Map<String, SimpleTriggerMate> simpleTrigger) {
    this.simpleTrigger = simpleTrigger;
  }

  /**
   * 描述 : 获取group
   *
   * @return the group
   */
  public Map<String, String> getGroup() {
    return group;
  }

  /**
   * 描述 : 设置group
   *
   * @param group the group to set
   */
  public void setGroup(Map<String, String> group) {
    this.group = group;
  }

  /**
   * 描述 : 获取jobDetail
   *
   * @return the jobDetail
   */
  public Map<String, JobDetailMate> getJobDetail() {
    return jobDetail;
  }

  /**
   * 描述 : 设置jobDetail
   *
   * @param jobDetail the jobDetail to set
   */
  public void setJobDetail(Map<String, JobDetailMate> jobDetail) {
    this.jobDetail = jobDetail;
  }

}
