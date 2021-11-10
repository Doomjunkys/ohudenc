/**
 * SchedulerProperties.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 描述 : SchedulerProperties
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.scheduler")
@Validated
public class SchedulerProperties {

  /**
   * 描述 : 是否自动启动
   */
  @NotNull
  private Boolean autoStartup = true;

  /**
   * 描述 : 是否覆盖已经存在的jobs
   */
  @NotNull
  private Boolean overwriteExistingJobs = true;

  /**
   * 描述 : 延迟启动秒数
   */
  @NotNull
  private Integer startupDelay = 0;

  /**
   * 描述 : Job接受applicationContext的成员变量名
   */
  @NotNull
  private String applicationContextSchedulerContextKey = "applicationContext";

  /**
   * 描述 : quartz配置文件地址
   */
  @NotNull
  private String quartzPropertiesPath = "def_quartz.properties";

  /**
   * 描述 : 获取autoStartup
   *
   * @return the autoStartup
   */
  public Boolean getAutoStartup() {
    return autoStartup;
  }

  /**
   * 描述 : 设置autoStartup
   *
   * @param autoStartup the autoStartup to set
   */
  public void setAutoStartup(Boolean autoStartup) {
    this.autoStartup = autoStartup;
  }

  /**
   * 描述 : 获取overwriteExistingJobs
   *
   * @return the overwriteExistingJobs
   */
  public Boolean getOverwriteExistingJobs() {
    return overwriteExistingJobs;
  }

  /**
   * 描述 : 设置overwriteExistingJobs
   *
   * @param overwriteExistingJobs the overwriteExistingJobs to set
   */
  public void setOverwriteExistingJobs(Boolean overwriteExistingJobs) {
    this.overwriteExistingJobs = overwriteExistingJobs;
  }

  /**
   * 描述 : 获取startupDelay
   *
   * @return the startupDelay
   */
  public Integer getStartupDelay() {
    return startupDelay;
  }

  /**
   * 描述 : 设置startupDelay
   *
   * @param startupDelay the startupDelay to set
   */
  public void setStartupDelay(Integer startupDelay) {
    this.startupDelay = startupDelay;
  }

  /**
   * 描述 : 获取applicationContextSchedulerContextKey
   *
   * @return the applicationContextSchedulerContextKey
   */
  public String getApplicationContextSchedulerContextKey() {
    return applicationContextSchedulerContextKey;
  }

  /**
   * 描述 : 设置applicationContextSchedulerContextKey
   *
   * @param applicationContextSchedulerContextKey the applicationContextSchedulerContextKey to set
   */
  public void setApplicationContextSchedulerContextKey(
      String applicationContextSchedulerContextKey) {
    this.applicationContextSchedulerContextKey = applicationContextSchedulerContextKey;
  }

  /**
   * 描述 : 获取quartzPropertiesPath
   *
   * @return the quartzPropertiesPath
   */
  public String getQuartzPropertiesPath() {
    return quartzPropertiesPath;
  }

  /**
   * 描述 : 设置quartzPropertiesPath
   *
   * @param quartzPropertiesPath the quartzPropertiesPath to set
   */
  public void setQuartzPropertiesPath(String quartzPropertiesPath) {
    this.quartzPropertiesPath = quartzPropertiesPath;
  }

}
