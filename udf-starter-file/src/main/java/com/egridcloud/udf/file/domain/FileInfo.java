/**
 * FileInfo.java
 * Created at 2017-05-27
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file.domain;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : FileInfo
 *
 * @author Administrator
 *
 */
@ApiModel(description = "文件信息")
public class FileInfo implements Serializable {

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
   * 描述 : 文件名称
   */
  @ApiModelProperty(value = "文件名称", required = true, dataType = "string")
  private String name;

  /**
   * 描述 : 文件相对路径
   */
  @ApiModelProperty(value = "文件相对路径", required = true, dataType = "string")
  private String relativePath;

  /**
   * 描述 : 上下文类型
   */
  @ApiModelProperty(value = "上下文类型", required = true, dataType = "string")
  private String contentType;

  /**
   * 描述 : 文件长度
   */
  @ApiModelProperty(value = "文件长度", required = true, dataType = "long")
  private Long size;

  /**
   * 描述 : 获取size
   *
   * @return the size
   */
  public Long getSize() {
    return size;
  }

  /**
   * 描述 : 设置size
   *
   * @param size the size to set
   */
  public void setSize(Long size) {
    this.size = size;
  }

  /**
   * 描述 : 获取contentType
   *
   * @return the contentType
   */
  public String getContentType() {
    return contentType;
  }

  /**
   * 描述 : 设置contentType
   *
   * @param contentType the contentType to set
   */
  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * 描述 : 获取relativePath
   *
   * @return the relativePath
   */
  public String getRelativePath() {
    return relativePath;
  }

  /**
   * 描述 : 设置relativePath
   *
   * @param relativePath the relativePath to set
   */
  public void setRelativePath(String relativePath) {
    this.relativePath = relativePath;
  }

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
