/**
 * RmsJobParam.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.domain;

import java.io.Serializable;
import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : RmsJobParam
 *
 * @author Administrator
 *
 */
@ApiModel(description = "通用job参数")
public class RmsJobParam implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 主键ID
   */
  @ApiModelProperty(value = "主键ID", required = true, dataType = "string")
  private String id;

  /**
   * 描述 : 触发实例ID
   */
  @ApiModelProperty(value = "触发实例ID", required = true, dataType = "string")
  private String fireInstanceId;

  /**
   * 描述 : 触发方式
   */
  @ApiModelProperty(value = "触发实例ID", required = true, dataType = "string")
  private String triggerType;

  /**
   * 描述 : bean名称
   */
  @ApiModelProperty(value = "bean名称", required = true, dataType = "string")
  private String beanName;

  /**
   * 描述 : 是否异步
   */
  @ApiModelProperty(value = "是否异步", required = true, dataType = "boolean")
  private Boolean async;

  /**
   * 描述 : 作业参数
   */
  @ApiModelProperty(value = "作业参数", required = true, dataType = "object")
  private Map<String, Object> jobDataMap; //NOSONAR

  /**
   * 描述 : 获取triggerType
   *
   * @return the triggerType
   */
  public String getTriggerType() {
    return triggerType;
  }

  /**
   * 描述 : 设置triggerType
   *
   * @param triggerType the triggerType to set
   */
  public void setTriggerType(String triggerType) {
    this.triggerType = triggerType;
  }

  /**
   * 描述 : 获取id
   *
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * 描述 : 设置id
   *
   * @param id the id to set
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * 描述 : 获取async
   *
   * @return the async
   */
  public Boolean getAsync() {
    return async;
  }

  /**
   * 描述 : 设置async
   *
   * @param async the async to set
   */
  public void setAsync(Boolean async) {
    this.async = async;
  }

  /**
   * 描述 : 获取jobDataMap
   *
   * @return the jobDataMap
   */
  public Map<String, Object> getJobDataMap() {
    return jobDataMap;
  }

  /**
   * 描述 : 设置jobDataMap
   *
   * @param jobDataMap the jobDataMap to set
   */
  public void setJobDataMap(Map<String, Object> jobDataMap) {
    this.jobDataMap = jobDataMap;
  }

  /**
   * 描述 : 获取fireInstanceId
   *
   * @return the fireInstanceId
   */
  public String getFireInstanceId() {
    return fireInstanceId;
  }

  /**
   * 描述 : 设置fireInstanceId
   *
   * @param fireInstanceId the fireInstanceId to set
   */
  public void setFireInstanceId(String fireInstanceId) {
    this.fireInstanceId = fireInstanceId;
  }

  /**
   * 描述 : 获取beanName
   *
   * @return the beanName
   */
  public String getBeanName() {
    return beanName;
  }

  /**
   * 描述 : 设置beanName
   *
   * @param beanName the beanName to set
   */
  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

}
