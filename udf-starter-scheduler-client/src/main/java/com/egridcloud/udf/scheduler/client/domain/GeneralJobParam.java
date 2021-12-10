/**
 * GeneralJobParam.java
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
 * 描述 : GeneralJobParam
 *
 * @author Administrator
 *
 */
@ApiModel(description = "通用job参数")
public class GeneralJobParam implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 触发实例ID
   */
  @ApiModelProperty(value = "触发实例ID", required = true, dataType = "string")
  private String fireInstanceId;

  /**
   * 描述 : bean名称
   */
  @ApiModelProperty(value = "bean名称", required = true, dataType = "string")
  private String beanName;

  /**
   * 描述 : 作业参数
   */
  @ApiModelProperty(value = "作业参数", required = true, dataType = "object")
  private Map<String, Object> jobDataMap; //NOSONAR

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
