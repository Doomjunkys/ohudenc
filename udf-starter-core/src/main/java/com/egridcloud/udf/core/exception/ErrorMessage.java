/**
 * ErrorMessage.java
 * Created at 2016-09-27
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

import java.io.Serializable;
import java.util.Date;

/**
 * 描述 : 错误信息
 *
 * @author wangkang
 *
 */
public class ErrorMessage implements Serializable {
  /**
   * 描述 : id
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 错误ID
   */
  private String id;

  /**
   * 描述 : http状态
   */
  private int httpStatus;

  /**
   * 描述 : 错误代码
   */
  private String errorCode;

  /**
   * 描述 : 错误描述
   */
  private String errorMsg;

  /**
   * 描述 : 错误发生时间
   */
  private Date errorDate;

  /**
   * 描述 : 异常类名
   */
  private String exceptionType;

  /**
   * 描述 : 异常信息
   */
  private String exceptionMessage;

  /**
   * 描述 : 详细异常堆栈信息
   */
  private String exceptionStackTrace;

  /**
   * 描述 : 资源部不在错误信息
   */
  private NoResourceFoundMessage noResourceFoundMessage;

  /**
   * 描述 : 参数校验错误信息
   */
  private ParameterValidErrorMessage parameterValidErrorMessage;

  /**
   * 描述 : http服务错误
   */
  private HttpErrorMessage httpErrorMessage;

  /**
   * 描述 : 获取httpErrorMessage
   *
   * @return the httpErrorMessage
   */
  public HttpErrorMessage getHttpErrorMessage() {
    return httpErrorMessage;
  }

  /**
   * 描述 : 设置httpErrorMessage
   *
   * @param httpErrorMessage the httpErrorMessage to set
   */
  public void setHttpErrorMessage(HttpErrorMessage httpErrorMessage) {
    this.httpErrorMessage = httpErrorMessage;
  }

  /**
   * 描述 : 获取noResourceFoundMessage
   *
   * @return the noResourceFoundMessage
   */
  public NoResourceFoundMessage getNoResourceFoundMessage() {
    return noResourceFoundMessage;
  }

  /**
   * 描述 : 设置noResourceFoundMessage
   *
   * @param noResourceFoundMessage the noResourceFoundMessage to set
   */
  public void setNoResourceFoundMessage(NoResourceFoundMessage noResourceFoundMessage) {
    this.noResourceFoundMessage = noResourceFoundMessage;
  }

  /**
   * 描述 : 获取parameterValidErrorMessage
   *
   * @return the parameterValidErrorMessage
   */
  public ParameterValidErrorMessage getParameterValidErrorMessage() {
    return parameterValidErrorMessage;
  }

  /**
   * 描述 : 设置parameterValidErrorMessage
   *
   * @param parameterValidErrorMessage the parameterValidErrorMessage to set
   */
  public void setParameterValidErrorMessage(ParameterValidErrorMessage parameterValidErrorMessage) {
    this.parameterValidErrorMessage = parameterValidErrorMessage;
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
   * 描述 : 获取errorDate
   *
   * @return the errorDate
   */
  public Date getErrorDate() {
    return errorDate;
  }

  /**
   * 描述 : 设置errorDate
   *
   * @param errorDate the errorDate to set
   */
  public void setErrorDate(Date errorDate) {
    this.errorDate = errorDate;
  }

  /**
   * 描述 : 获取httpStatus
   *
   * @return the httpStatus
   */
  public int getHttpStatus() {
    return httpStatus;
  }

  /**
   * 描述 : 设置httpStatus
   *
   * @param httpStatus the httpStatus to set
   */
  public void setHttpStatus(int httpStatus) {
    this.httpStatus = httpStatus;
  }

  /**
   * 描述 : 获取errorCode
   *
   * @return the errorCode
   */
  public String getErrorCode() {
    return errorCode;
  }

  /**
   * 描述 : 设置errorCode
   *
   * @param errorCode the errorCode to set
   */
  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  /**
   * 描述 : 获取errorMsg
   *
   * @return the errorMsg
   */
  public String getErrorMsg() {
    return errorMsg;
  }

  /**
   * 描述 : 设置errorMsg
   *
   * @param errorMsg the errorMsg to set
   */
  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
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
