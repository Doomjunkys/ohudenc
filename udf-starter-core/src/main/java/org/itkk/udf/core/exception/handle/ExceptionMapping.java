/**
 * ExceptionMapping.java
 * Created at 2016-09-20
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.core.exception.handle;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.servlet.NoHandlerFoundException;

import org.itkk.udf.core.ApplicationConfig;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.AuthException;
import org.itkk.udf.core.exception.ErrorCode;
import org.itkk.udf.core.exception.ErrorResult;
import org.itkk.udf.core.exception.ParameterValidException;
import org.itkk.udf.core.exception.PermissionException;
import org.itkk.udf.core.exception.SystemException;
import org.itkk.udf.core.exception.SystemRuntimeException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
   * 描述 : objectMapper
   */
  @Autowired
  private ObjectMapper objectMapper;

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
   * @throws IOException 异常
   */
  @ExceptionHandler(value = RestClientResponseException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<String> restClientResponseException(RestClientResponseException exception)
      throws IOException {
    //获得错误
    ErrorResult errorResult = buildError(exception);
    //获得子错误
    String result = exception.getResponseBodyAsString();
    if (StringUtils.isNotBlank(result)) {
      //解析子错误
      RestResponse<String> child = objectMapper.readValue(exception.getResponseBodyAsString(),
          objectMapper.getTypeFactory().constructParametricType(RestResponse.class, String.class));
      //设置子错误
      errorResult.setChild(child);
    }
    //返回
    return new RestResponse<>(ErrorCode.HTTP_ERROR, errorResult);
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
   * 描述 : 捕获HttpMediaTypeNotAcceptableException异常(Http媒体类型不可接受)
   *
   * @param exception 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = HttpMediaTypeNotAcceptableException.class)
  @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
  @ResponseBody
  public RestResponse<String> httpMediaTypeNotAcceptableException(
      HttpMediaTypeNotAcceptableException exception) {
    return new RestResponse<>(ErrorCode.HTTP_MEDIA_TYPE_NOT_ACCEPTABLE, buildError(exception));
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
    error.setType(exception.getClass().getName());
    error.setMessage(ExceptionUtils.getMessage(exception));
    if (applicationConfig.isOutputExceptionStackTrace()) {
      error.setStackTrace(ExceptionUtils.getStackTrace(exception));
    }
    error.setDate(new Date());
    LOGGER.error(exception.getClass().getName(), exception);
    return error;
  }

}
