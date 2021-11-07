/**
 * InlineInfo.java
 * Created at 2017-05-29
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.domain.mail;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : InlineInfo
 *
 * @author Administrator
 *
 */
@ApiModel(description = "静态资源信息")
public class InlineInfo implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 静态资源名称
   */
  @ApiModelProperty(value = "静态资源名称", required = true, dataType = "string")
  private String name;

  /**
   * 描述 : 附件文件ID
   */
  @ApiModelProperty(value = "静态资源名称文件ID", required = true, dataType = "string")
  private String fileId;

  /**
   * 描述 : 获取name
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * 描述 : 设置name
   *
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * 描述 : 获取fileId
   *
   * @return the fileId
   */
  public String getFileId() {
    return fileId;
  }

  /**
   * 描述 : 设置fileId
   *
   * @param fileId the fileId to set
   */
  public void setFileId(String fileId) {
    this.fileId = fileId;
  }

}
