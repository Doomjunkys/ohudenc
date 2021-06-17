/**
 * ExceptionMapping.java
 * Created at 2016-09-20
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception.handle;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.egridcloud.udf.core.ApplicationConfig;
import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.core.exception.AuthException;
import com.egridcloud.udf.core.exception.ErrorCode;
import com.egridcloud.udf.core.exception.ErrorMessage;
import com.egridcloud.udf.core.exception.HttpServerErrorMessage;
import com.egridcloud.udf.core.exception.NoResourceFoundMessage;
import com.egridcloud.udf.core.exception.ParameterValidErrorMessage;
import com.egridcloud.udf.core.exception.ParameterValidException;
import com.egridcloud.udf.core.exception.PermissionException;
import com.egridcloud.udf.core.exception.SystemException;
import com.egridcloud.udf.core.exception.SystemRuntimeException;
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
   * 描述 : json解析器
   */
  @Autowired
  private ObjectMapper xssObjectMapper;

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
   * @param permissionException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = PermissionException.class)
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ResponseBody
  public RestResponse<ErrorMessage> permissionException(PermissionException permissionException) {
    String returnCode = ErrorCode.PERMISSION_ERROR.name();
    String returnMessage = ErrorCode.PERMISSION_ERROR.value();
    ErrorMessage em = ExceptionMapping.buildErrorMessage(HttpStatus.FORBIDDEN.value(), returnCode,
        returnMessage, applicationConfig.isOutputExceptionStackTrace(), permissionException);
    LOGGER.error(em.getId(), permissionException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获AuthException异常(认证异常)
   *
   * @param authException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = AuthException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  @ResponseBody
  public RestResponse<ErrorMessage> authException(AuthException authException) {
    String returnCode = ErrorCode.AUTH_ERROR.name();
    String returnMessage = ErrorCode.AUTH_ERROR.value();
    ErrorMessage em = ExceptionMapping.buildErrorMessage(HttpStatus.UNAUTHORIZED.value(),
        returnCode, returnMessage, applicationConfig.isOutputExceptionStackTrace(), authException);
    LOGGER.error(em.getId(), authException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获ParameterValidException异常(参数校验错误)
   *
   * @param parameterValidException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = ParameterValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public RestResponse<ErrorMessage> parameterValidException(
      ParameterValidException parameterValidException) {
    String returnCode = ErrorCode.PARAMETER_VALID_ERROR.name();
    String returnMessage = ErrorCode.PARAMETER_VALID_ERROR.value();
    ErrorMessage em = ExceptionMapping.buildErrorMessage(HttpStatus.BAD_REQUEST.value(), returnCode,
        returnMessage, applicationConfig.isOutputExceptionStackTrace(), parameterValidException);
    em.setParameterValidErrorMessage(new ParameterValidErrorMessage(parameterValidException));
    LOGGER.error(em.getId(), parameterValidException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获HttpServerErrorException异常(http服务异常)
   *
   * @param httpServerErrorException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = HttpServerErrorException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<ErrorMessage> httpServerErrorException(
      HttpServerErrorException httpServerErrorException) {
    String returnCode = ErrorCode.HTTP_SERVER_ERROR.name();
    String returnMessage = ErrorCode.HTTP_SERVER_ERROR.value();
    ErrorMessage em = ExceptionMapping.buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        returnCode, returnMessage, applicationConfig.isOutputExceptionStackTrace(),
        httpServerErrorException);
    em.setHttpServerErrorMessage(
        new HttpServerErrorMessage(httpServerErrorException, xssObjectMapper));
    LOGGER.error(em.getId(), httpServerErrorException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获NoHandlerFoundException异常(资源不存在)
   *
   * @param noHandlerFoundException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = NoHandlerFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ResponseBody
  public RestResponse<ErrorMessage> noHandlerFoundException(
      NoHandlerFoundException noHandlerFoundException) {
    String returnCode = ErrorCode.NO_RESOURCE_FOUND.name();
    String returnMessage = ErrorCode.NO_RESOURCE_FOUND.value();
    ErrorMessage em = ExceptionMapping.buildErrorMessage(HttpStatus.NOT_FOUND.value(), returnCode,
        returnMessage, applicationConfig.isOutputExceptionStackTrace(), noHandlerFoundException);
    em.setNoResourceFoundMessage(new NoResourceFoundMessage(noHandlerFoundException));
    LOGGER.error(em.getId(), noHandlerFoundException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获SystemException异常(系统检查型异常)
   *
   * @param systemException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = SystemException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<ErrorMessage> systemException(SystemException systemException) {
    String returnCode = ErrorCode.ERROR.name();
    String returnMessage = ErrorCode.ERROR.value();
    ErrorMessage em =
        ExceptionMapping.buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), returnCode,
            returnMessage, applicationConfig.isOutputExceptionStackTrace(), systemException);
    LOGGER.error(em.getId(), systemException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获SystemRuntimeException异常(系统运行时型异常)
   *
   * @param systemRuntimeException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = SystemRuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<ErrorMessage> systemRuntimeException(
      SystemRuntimeException systemRuntimeException) {
    String returnCode = ErrorCode.ERROR.name();
    String returnMessage = ErrorCode.ERROR.value();
    ErrorMessage em =
        ExceptionMapping.buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), returnCode,
            returnMessage, applicationConfig.isOutputExceptionStackTrace(), systemRuntimeException);
    LOGGER.error(em.getId(), systemRuntimeException);
    return new RestResponse<>(returnCode, returnMessage, em);
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
  public RestResponse<ErrorMessage> exception(Exception exception) {
    String returnCode = ErrorCode.ERROR.name();
    String returnMessage = ErrorCode.ERROR.value();
    ErrorMessage em = ExceptionMapping.buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(),
        returnCode, returnMessage, applicationConfig.isOutputExceptionStackTrace(), exception);
    LOGGER.error(em.getId(), exception);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 捕获RuntimeException异常(运行时异常)
   *
   * @param runtimeException 异常
   * @return 错误信息
   */
  @ExceptionHandler(value = RuntimeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public RestResponse<ErrorMessage> runtimeException(RuntimeException runtimeException) {
    String returnCode = ErrorCode.ERROR.name();
    String returnMessage = ErrorCode.ERROR.value();
    ErrorMessage em =
        ExceptionMapping.buildErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), returnCode,
            returnMessage, applicationConfig.isOutputExceptionStackTrace(), runtimeException);
    LOGGER.error(em.getId(), runtimeException);
    return new RestResponse<>(returnCode, returnMessage, em);
  }

  /**
   * 描述 : 构造错误信息(部分)
   *
   * @param httpStatus http状态
   * @param errorCode 错误代码
   * @param errorMessage 错误描述
   * @param outputExceptionStackTrace 是否输出异常堆栈
   * @param exception 异常
   * @return 错误信息
   */
  public static ErrorMessage buildErrorMessage(int httpStatus, String errorCode,
      String errorMessage, boolean outputExceptionStackTrace, Exception exception) {
    ErrorMessage em = new ErrorMessage();
    em.setId(UUID.randomUUID().toString());
    em.setHttpStatus(httpStatus);
    em.setErrorCode(errorCode);
    em.setErrorMsg(errorMessage);
    em.setExceptionType(exception.getClass().getName());
    em.setExceptionMessage(ExceptionUtils.getMessage(exception));
    em.setErrorDate(new Date());
    if (outputExceptionStackTrace) {
      em.setExceptionStackTrace(ExceptionUtils.getStackTrace(exception));
    }
    return em;
  }

}
