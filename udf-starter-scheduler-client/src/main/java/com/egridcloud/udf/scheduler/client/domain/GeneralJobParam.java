/**
 * GeneralJobParam.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : GeneralJobParam
 *
 * @author Administrator
 *
 */
@ApiModel(description = "通用作业参数")
public class GeneralJobParam implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 执行ID
   */
  @ApiModelProperty(value = "执行ID", required = true, dataType = "string")
  private String id = UUID.randomUUID().toString();

  /**
   * 描述 : bean名称
   */
  @ApiModelProperty(value = "bean名称", required = true, dataType = "string")
  private String beanName;

  /**
   * 描述 : 服务端初始化开始时间
   */
  @ApiModelProperty(value = "服务端初始化开始时间", required = true, dataType = "date")
  private Date serverInitStartDate;

  /**
   * 描述 : 服务端开始执行时间
   */
  @ApiModelProperty(value = "服务端开始执行时间", required = true, dataType = "date")
  private Date serverStartDate;

  /**
   * 描述 : 获取serverInitStartDate
   *
   * @return the serverInitStartDate
   */
  public Date getServerInitStartDate() {
    return serverInitStartDate;
  }

  /**
   * 描述 : 设置serverInitStartDate
   *
   * @param serverInitStartDate the serverInitStartDate to set
   */
  public void setServerInitStartDate(Date serverInitStartDate) {
    this.serverInitStartDate = serverInitStartDate;
  }

  /**
   * 描述 : 获取serverStartDate
   *
   * @return the serverStartDate
   */
  public Date getServerStartDate() {
    return serverStartDate;
  }

  /**
   * 描述 : 设置serverStartDate
   *
   * @param serverStartDate the serverStartDate to set
   */
  public void setServerStartDate(Date serverStartDate) {
    this.serverStartDate = serverStartDate;
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
