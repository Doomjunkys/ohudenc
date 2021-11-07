/**
 * FileParam.java
 * Created at 2017-05-27
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.domain.file;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : FileParam
 *
 * @author Administrator
 *
 */
@ApiModel(description = "文件参数")
public class FileParam implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 文件ID
   */
  @ApiModelProperty(value = "文件ID(相对路径的base64编码)", required = true, dataType = "string")
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
