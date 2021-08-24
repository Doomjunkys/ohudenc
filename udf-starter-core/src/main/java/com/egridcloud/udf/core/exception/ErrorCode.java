/**
 * ErrorCode.java
 * Created at 2016-09-27
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

/**
 * 描述 : 错误代码枚举
 *
 * @author wangkang
 *
 */
public enum ErrorCode {
  /**
   * 描述 : 系统内部错误
   */
  ERROR("system internal error"),
  /**
   * 描述 : http服务错误
   */
  HTTP_ERROR("http error"),
  /**
   * 描述 : 参数校验错误
   */
  PARAMETER_VALID_ERROR("parameter check error"),
  /**
   * 描述 : 方法不允许
   */
  METHOD_NOT_ALLOWED("method not allowed"),
  /**
   * 描述 : Http媒体类型不可接受
   */
  HTTP_MEDIA_TYPE_NOT_ACCEPTABLE("Http Media Type Not Acceptable"),
  /**
   * 描述 : 资源不存在
   */
  NO_RESOURCE_FOUND("resource does not exist"),
  /**
   * 描述 : 认证错误
   */
  AUTH_ERROR("auth error"),
  /**
   * 描述 : 权限错误
   */
  PERMISSION_ERROR("Permission error");

  /**
   * <p>
   * Field value: 参数值
   * </p>
   */
  private String value = null;

  /**
   * <p>
   * Description: 构造函数
   * </p>
   * 
   * @param value 构造函数
   */
  private ErrorCode(String value) {
    this.value = value;
  }

  /**
   * <p>
   * Description: 放回int值
   * </p>
   * 
   * @return value 放回int值
   */
  public String value() {
    return this.value;
  }
}
