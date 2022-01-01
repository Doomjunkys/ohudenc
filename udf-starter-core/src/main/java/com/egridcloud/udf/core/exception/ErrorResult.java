/**
 * ErrorResult.java
 * Created at 2016-09-27
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

import java.io.Serializable;
import java.util.Date;

import com.egridcloud.udf.core.RestResponse;

import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : 错误信息
 *
 * @author wangkang
 *
 */
public class ErrorResult implements Serializable {
  /**
   * 描述 : id
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 异常时间
   */
  @ApiModelProperty(value = "异常时间", required = true, dataType = "date")
  private Date date;

  /**
   * 描述 : 异常类名
   */
  @ApiModelProperty(value = "异常类名", required = true, dataType = "string")
  private String type;

  /**
   * 描述 : 异常信息
   */
  @ApiModelProperty(value = "异常信息", required = true, dataType = "string")
  private String message;

  /**
   * 描述 : 详细异常堆栈信息
   */
  @ApiModelProperty(value = "异常堆栈", required = true, dataType = "string")
  private String stackTrace;

  /**
   * 描述 : 子异常
   */
  @ApiModelProperty(value = "子异常", required = true, dataType = "object")
  private RestResponse<String> child;

  /**
   * 描述 : 获取child
   *
   * @return the child
   */
  public RestResponse<String> getChild() {
    return child;
  }

  /**
   * 描述 : 设置child
   *
   * @param child the child to set
   */
  public void setChild(RestResponse<String> child) {
    this.child = child;
  }

  /**
   * 描述 : 获取date
   *
   * @return the date
   */
  public Date getDate() {
    return date;
  }

  /**
   * 描述 : 设置date
   *
   * @param date the date to set
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * 描述 : 获取type
   *
   * @return the type
   */
  public String getType() {
    return type;
  }

  /**
   * 描述 : 设置type
   *
   * @param type the type to set
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * 描述 : 获取message
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * 描述 : 设置message
   *
   * @param message the message to set
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * 描述 : 获取stackTrace
   *
   * @return the stackTrace
   */
  public String getStackTrace() {
    return stackTrace;
  }

  /**
   * 描述 : 设置stackTrace
   *
   * @param stackTrace the stackTrace to set
   */
  public void setStackTrace(String stackTrace) {
    this.stackTrace = stackTrace;
  }

}
