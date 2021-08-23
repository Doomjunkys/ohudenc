/**
 * ErrorResult.java
 * Created at 2016-09-27
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

import java.io.Serializable;
import java.util.Date;

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
  private Date exceptionDate;

  /**
   * 描述 : 异常类名
   */
  @ApiModelProperty(value = "异常类名", required = true, dataType = "string")
  private String exceptionType;

  /**
   * 描述 : 异常信息
   */
  @ApiModelProperty(value = "异常信息", required = true, dataType = "string")
  private String exceptionMessage;

  /**
   * 描述 : 详细异常堆栈信息
   */
  @ApiModelProperty(value = "异常堆栈", required = true, dataType = "string")
  private String exceptionStackTrace;

  /**
   * 描述 : 获取exceptionDate
   *
   * @return the exceptionDate
   */
  public Date getExceptionDate() {
    return exceptionDate;
  }

  /**
   * 描述 : 设置exceptionDate
   *
   * @param exceptionDate the exceptionDate to set
   */
  public void setExceptionDate(Date exceptionDate) {
    this.exceptionDate = exceptionDate;
  }

  /**
   * 描述 : 获取exceptionType
   *
   * @return the exceptionType
   */
  public String getExceptionType() {
    return exceptionType;
  }

  /**
   * 描述 : 设置exceptionType
   *
   * @param exceptionType the exceptionType to set
   */
  public void setExceptionType(String exceptionType) {
    this.exceptionType = exceptionType;
  }

  /**
   * 描述 : 获取exceptionMessage
   *
   * @return the exceptionMessage
   */
  public String getExceptionMessage() {
    return exceptionMessage;
  }

  /**
   * 描述 : 设置exceptionMessage
   *
   * @param exceptionMessage the exceptionMessage to set
   */
  public void setExceptionMessage(String exceptionMessage) {
    this.exceptionMessage = exceptionMessage;
  }

  /**
   * 描述 : 获取exceptionStackTrace
   *
   * @return the exceptionStackTrace
   */
  public String getExceptionStackTrace() {
    return exceptionStackTrace;
  }

  /**
   * 描述 : 设置exceptionStackTrace
   *
   * @param exceptionStackTrace the exceptionStackTrace to set
   */
  public void setExceptionStackTrace(String exceptionStackTrace) {
    this.exceptionStackTrace = exceptionStackTrace;
  }

}
