/**
 * GeneralJobParam.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.domain;

import java.io.Serializable;

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
  private String id;

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

}
