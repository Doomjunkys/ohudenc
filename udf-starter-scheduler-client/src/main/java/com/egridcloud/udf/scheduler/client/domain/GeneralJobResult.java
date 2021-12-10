/**
 * GeneralJobParam.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : GeneralJobParam
 *
 * @author Administrator
 *
 */
@ApiModel(description = "通用job返回值")
public class GeneralJobResult implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 执行ID
   */
  @ApiModelProperty(value = "执行ID", required = true, dataType = "string")
  private String fireInstanceId;

  /**
   * 描述 : 参数
   */
  @ApiModelProperty(value = "参数", required = true, dataType = "object")
  private GeneralJobParam param;

  /**
   * 描述 : 客户端初始化时间
   */
  @ApiModelProperty(value = "客户端初始化时间", required = true, dataType = "long")
  private Date clientInitTime;

  /**
   * 描述 : 客户端开始时间
   */
  @ApiModelProperty(value = "客户端开始时间", required = true, dataType = "long")
  private Date clientStartExecuteTime;

  /**
   * 描述 : 客户端结束时间
   */
  @ApiModelProperty(value = "客户端结束时间", required = true, dataType = "long")
  private Date clientEndExecuteTime;

  /**
   * 描述 : 获取clientInitTime
   *
   * @return the clientInitTime
   */
  public Date getClientInitTime() {
    return clientInitTime;
  }

  /**
   * 描述 : 设置clientInitTime
   *
   * @param clientInitTime the clientInitTime to set
   */
  public void setClientInitTime(Date clientInitTime) {
    this.clientInitTime = clientInitTime;
  }

  /**
   * 描述 : 获取clientStartExecuteTime
   *
   * @return the clientStartExecuteTime
   */
  public Date getClientStartExecuteTime() {
    return clientStartExecuteTime;
  }

  /**
   * 描述 : 设置clientStartExecuteTime
   *
   * @param clientStartExecuteTime the clientStartExecuteTime to set
   */
  public void setClientStartExecuteTime(Date clientStartExecuteTime) {
    this.clientStartExecuteTime = clientStartExecuteTime;
  }

  /**
   * 描述 : 获取clientEndExecuteTime
   *
   * @return the clientEndExecuteTime
   */
  public Date getClientEndExecuteTime() {
    return clientEndExecuteTime;
  }

  /**
   * 描述 : 设置clientEndExecuteTime
   *
   * @param clientEndExecuteTime the clientEndExecuteTime to set
   */
  public void setClientEndExecuteTime(Date clientEndExecuteTime) {
    this.clientEndExecuteTime = clientEndExecuteTime;
  }

  /**
   * 描述 : 获取param
   *
   * @return the param
   */
  public GeneralJobParam getParam() {
    return param;
  }

  /**
   * 描述 : 设置param
   *
   * @param param the param to set
   */
  public void setParam(GeneralJobParam param) {
    this.param = param;
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

}
