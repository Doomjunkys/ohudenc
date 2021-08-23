/**
 * ExceptionMapping.java
 * Created at 2016-09-20
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception.handle;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.egridcloud.udf.core.ApplicationConfig;
import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.core.exception.AuthException;
import com.egridcloud.udf.core.exception.ErrorCode;
import com.egridcloud.udf.core.exception.ErrorResult;
import com.egridcloud.udf.core.exception.ParameterValidException;
import com.egridcloud.udf.core.exception.PermissionException;
import com.egridcloud.udf.core.exception.SystemException;
import com.egridcloud.udf.core.exception.SystemRuntimeException;

/**
 * 描述 : 全局异常处理
 *
 * @author wangkang
 *
 */
@ControllerAdvice
public class ExceptionMapping {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionMapping.class);

  /**
   * 描述 : 系统配置
   */
  @Autowired
  private ApplicationConfig applicationConfig;

  /**
   * 描述 : 构造函数
   *
   */
  public ExceptionMapping() {
    LOGGER.info("ExceptionMapping initialize");
  }

  /**
   * 描述 : 捕获PermissionException异常(权限异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = PermissionException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public RestResponse<String> permissionException(PermissionException exception) {
    return new RestResponse<>(ErrorCode.PERMISSION_ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获AuthException异常(认证异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = AuthException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public RestResponse<String> authException(AuthException exception) {
    return new RestResponse<>(ErrorCode.AUTH_ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获ParameterValidException异常(参数校验错误)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = ParameterValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public RestResponse<String> parameterValidException(ParameterValidException exception) {
    return new RestResponse<>(ErrorCode.PARAMETER_VALID_ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获RestClientResponseException异常(http服务异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = RestClientResponseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<String> restClientResponseException(RestClientResponseException exception) {
    return new RestResponse<>(ErrorCode.HTTP_ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获NoHandlerFoundException异常(资源不存在)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public RestResponse<String> noHandlerFoundException(NoHandlerFoundException exception) {
    return new RestResponse<>(ErrorCode.NO_RESOURCE_FOUND, buildError(exception));
  }

  /**
   * 描述 : 捕获httpRequestMethodNotSupportedException异常(方法不允许)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
  @ResponseBody
  public RestResponse<String> httpRequestMethodNotSupportedException(
      HttpRequestMethodNotSupportedException exception) {
    return new RestResponse<>(ErrorCode.METHOD_NOT_ALLOWED, buildError(exception));
  }

  /**
   * 描述 : 捕获SystemException异常(系统检查型异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = SystemException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<String> systemException(SystemException exception) {
    return new RestResponse<>(ErrorCode.ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获SystemRuntimeException异常(系统运行时型异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = SystemRuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<String> systemRuntimeException(SystemRuntimeException exception) {
    return new RestResponse<>(ErrorCode.ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获Exception异常(检查型异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<String> exception(Exception exception) {
    return new RestResponse<>(ErrorCode.ERROR, buildError(exception));
  }

  /**
   * 描述 : 捕获RuntimeException异常(运行时异常)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<String> runtimeException(RuntimeException exception) {
    return new RestResponse<>(ErrorCode.ERROR, buildError(exception));
  }

  /**
   * 描述 : 构造错误响应对象
   *
   * @param exception 异常
   * @return 错误响应对象
   */
  private ErrorResult buildError(Exception exception) {
    ErrorResult error = new ErrorResult();
    error.setExceptionType(exception.getClass().getName());
    error.setExceptionMessage(ExceptionUtils.getMessage(exception));
    if (applicationConfig.isOutputExceptionStackTrace()) {
      error.setExceptionStackTrace(ExceptionUtils.getStackTrace(exception));
    }
    error.setExceptionDate(new Date());
    LOGGER.error(exception.getClass().getName(), exception);
    return error;
  }

}
