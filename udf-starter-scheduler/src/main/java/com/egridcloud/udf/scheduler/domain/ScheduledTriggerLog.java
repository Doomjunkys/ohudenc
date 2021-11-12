/**
 * TriggerDetailListener.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : ScheduledTriggerLog
 *
 * @author Administrator
 *
 */
@ApiModel(description = "计划任务触发日志")
public class ScheduledTriggerLog implements Serializable {

  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 日志ID
   */
  @ApiModelProperty(value = "日志ID", required = true, dataType = "string")
  private String logid;

  /**
   * 描述 : 计划执行时间
   */
  @ApiModelProperty(value = "计划执行时间", required = true, dataType = "date")
  private Date scheduledFireTime;

  /**
   * 描述 : 实际执行时间
   */
  @ApiModelProperty(value = "实际执行时间", required = true, dataType = "date")
  private Date fireTime;

  /**
   * 描述 : 实际结束时间
   */
  @ApiModelProperty(value = "实际结束时间", required = true, dataType = "date")
  private Date endTime;

  /**
   * 描述 : 执行时长
   */
  @ApiModelProperty(value = "执行时长", required = true, dataType = "long")
  private Long jobRunTime;

  /**
   * 描述 : 状态
   */
  @ApiModelProperty(value = "状态", required = true, dataType = "String")
  private String status;

  /**
   * 描述 : 结果
   */
  @ApiModelProperty(value = "结果", required = true, dataType = "String")
  private String result;

  /**
   * 描述 : 错误信息
   */
  @ApiModelProperty(value = "错误信息", required = true, dataType = "String")
  private String errorMsg;

  /**
   * 描述 : 触发器名称
   */
  @ApiModelProperty(value = "触发器名称", required = true, dataType = "String")
  private String triggerName;

  /**
   * 描述 : 触发器组别
   */
  @ApiModelProperty(value = "触发器组别", required = true, dataType = "String")
  private String triggerGroup;

  /**
   * 描述 : 作业名称
   */
  @ApiModelProperty(value = "作业名称", required = true, dataType = "String")
  private String jobName;

  /**
   * 描述 : 作业组别
   */
  @ApiModelProperty(value = "作业组别", required = true, dataType = "String")
  private String jobGroup;

  /**
   * 描述 : 作业类型
   */
  @ApiModelProperty(value = "作业类型", required = true, dataType = "String")
  private String jobClass;

  /**
   * 描述 : 线程组名称
   */
  @ApiModelProperty(value = "线程组名称", required = true, dataType = "String")
  private String threadGroupName;

  /**
   * 描述 : 线程ID
   */
  @ApiModelProperty(value = "线程ID", required = true, dataType = "String")
  private String threadId;

  /**
   * 描述 : 线程名称
   */
  @ApiModelProperty(value = "线程名称", required = true, dataType = "String")
  private String threadName;

  /**
   * 描述 : 线程优先级
   */
  @ApiModelProperty(value = "线程优先级", required = true, dataType = "String")
  private String threadPriority;

  /**
   * 描述 : 计划任务ID
   */
  @ApiModelProperty(value = "计划任务ID", required = true, dataType = "String")
  private String scheduledId;

  /**
   * 描述 : 计划任务名称
   */
  @ApiModelProperty(value = "计划任务名称", required = true, dataType = "String")
  private String scheduledName;

  /**
   * 描述 : 创建时间
   */
  @ApiModelProperty(value = "创建时间", required = true, dataType = "date")
  private Date createDate;

  /**
   * 描述 : 获取logid
   *
   * @return the logid
   */
  public String getLogid() {
    return logid;
  }

  /**
   * 描述 : 设置logid
   *
   * @param logid the logid to set
   */
  public void setLogid(String logid) {
    this.logid = logid;
  }

  /**
   * 描述 : 获取scheduledFireTime
   *
   * @return the scheduledFireTime
   */
  public Date getScheduledFireTime() {
    return scheduledFireTime;
  }

  /**
   * 描述 : 设置scheduledFireTime
   *
   * @param scheduledFireTime the scheduledFireTime to set
   */
  public void setScheduledFireTime(Date scheduledFireTime) {
    this.scheduledFireTime = scheduledFireTime;
  }

  /**
   * 描述 : 获取fireTime
   *
   * @return the fireTime
   */
  public Date getFireTime() {
    return fireTime;
  }

  /**
   * 描述 : 设置fireTime
   *
   * @param fireTime the fireTime to set
   */
  public void setFireTime(Date fireTime) {
    this.fireTime = fireTime;
  }

  /**
   * 描述 : 获取endTime
   *
   * @return the endTime
   */
  public Date getEndTime() {
    return endTime;
  }

  /**
   * 描述 : 设置endTime
   *
   * @param endTime the endTime to set
   */
  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /**
   * 描述 : 获取jobRunTime
   *
   * @return the jobRunTime
   */
  public Long getJobRunTime() {
    return jobRunTime;
  }

  /**
   * 描述 : 设置jobRunTime
   *
   * @param jobRunTime the jobRunTime to set
   */
  public void setJobRunTime(Long jobRunTime) {
    this.jobRunTime = jobRunTime;
  }

  /**
   * 描述 : 获取status
   *
   * @return the status
   */
  public String getStatus() {
    return status;
  }

  /**
   * 描述 : 设置status
   *
   * @param status the status to set
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * 描述 : 获取result
   *
   * @return the result
   */
  public String getResult() {
    return result;
  }

  /**
   * 描述 : 设置result
   *
   * @param result the result to set
   */
  public void setResult(String result) {
    this.result = result;
  }

  /**
   * 描述 : 获取errorMsg
   *
   * @return the errorMsg
   */
  public String getErrorMsg() {
    return errorMsg;
  }

  /**
   * 描述 : 设置errorMsg
   *
   * @param errorMsg the errorMsg to set
   */
  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  /**
   * 描述 : 获取triggerName
   *
   * @return the triggerName
   */
  public String getTriggerName() {
    return triggerName;
  }

  /**
   * 描述 : 设置triggerName
   *
   * @param triggerName the triggerName to set
   */
  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }

  /**
   * 描述 : 获取triggerGroup
   *
   * @return the triggerGroup
   */
  public String getTriggerGroup() {
    return triggerGroup;
  }

  /**
   * 描述 : 设置triggerGroup
   *
   * @param triggerGroup the triggerGroup to set
   */
  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }

  /**
   * 描述 : 获取jobName
   *
   * @return the jobName
   */
  public String getJobName() {
    return jobName;
  }

  /**
   * 描述 : 设置jobName
   *
   * @param jobName the jobName to set
   */
  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  /**
   * 描述 : 获取jobGroup
   *
   * @return the jobGroup
   */
  public String getJobGroup() {
    return jobGroup;
  }

  /**
   * 描述 : 设置jobGroup
   *
   * @param jobGroup the jobGroup to set
   */
  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }

  /**
   * 描述 : 获取jobClass
   *
   * @return the jobClass
   */
  public String getJobClass() {
    return jobClass;
  }

  /**
   * 描述 : 设置jobClass
   *
   * @param jobClass the jobClass to set
   */
  public void setJobClass(String jobClass) {
    this.jobClass = jobClass;
  }

  /**
   * 描述 : 获取threadGroupName
   *
   * @return the threadGroupName
   */
  public String getThreadGroupName() {
    return threadGroupName;
  }

  /**
   * 描述 : 设置threadGroupName
   *
   * @param threadGroupName the threadGroupName to set
   */
  public void setThreadGroupName(String threadGroupName) {
    this.threadGroupName = threadGroupName;
  }

  /**
   * 描述 : 获取threadId
   *
   * @return the threadId
   */
  public String getThreadId() {
    return threadId;
  }

  /**
   * 描述 : 设置threadId
   *
   * @param threadId the threadId to set
   */
  public void setThreadId(String threadId) {
    this.threadId = threadId;
  }

  /**
   * 描述 : 获取threadName
   *
   * @return the threadName
   */
  public String getThreadName() {
    return threadName;
  }

  /**
   * 描述 : 设置threadName
   *
   * @param threadName the threadName to set
   */
  public void setThreadName(String threadName) {
    this.threadName = threadName;
  }

  /**
   * 描述 : 获取threadPriority
   *
   * @return the threadPriority
   */
  public String getThreadPriority() {
    return threadPriority;
  }

  /**
   * 描述 : 设置threadPriority
   *
   * @param threadPriority the threadPriority to set
   */
  public void setThreadPriority(String threadPriority) {
    this.threadPriority = threadPriority;
  }

  /**
   * 描述 : 获取scheduledId
   *
   * @return the scheduledId
   */
  public String getScheduledId() {
    return scheduledId;
  }

  /**
   * 描述 : 设置scheduledId
   *
   * @param scheduledId the scheduledId to set
   */
  public void setScheduledId(String scheduledId) {
    this.scheduledId = scheduledId;
  }

  /**
   * 描述 : 获取scheduledName
   *
   * @return the scheduledName
   */
  public String getScheduledName() {
    return scheduledName;
  }

  /**
   * 描述 : 设置scheduledName
   *
   * @param scheduledName the scheduledName to set
   */
  public void setScheduledName(String scheduledName) {
    this.scheduledName = scheduledName;
  }

  /**
   * 描述 : 获取createDate
   *
   * @return the createDate
   */
  public Date getCreateDate() {
    return createDate;
  }

  /**
   * 描述 : 设置createDate
   *
   * @param createDate the createDate to set
   */
  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
