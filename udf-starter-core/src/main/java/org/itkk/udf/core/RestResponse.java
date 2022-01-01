/**
 * RestResponse.java
 * Created at 2016-09-19
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.core;

import java.io.Serializable;
import java.util.UUID;

import org.itkk.udf.core.exception.ErrorCode;
import org.itkk.udf.core.exception.ErrorResult;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 描述 : rest响应对象
 *
 * 
 * 
 * @author wangkang
 *
 * 
 * 
 */
@ApiModel(description = "响应消息体")
public class RestResponse<T> implements Serializable {

  /**
   * 
   * 描述 : id
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 响应ID
   */
  @ApiModelProperty(value = "响应ID", required = true, dataType = "string")
  private String id = UUID.randomUUID().toString();

  /**
   * 
   * 描述 : 状态码(业务定义)
   * 
   */
  @ApiModelProperty(value = "状态码(业务定义)", required = true, dataType = "string")
  private String code = "SUCCESS";

  /**
   * 
   * 描述 : 状态码描述(业务定义)
   * 
   */
  @ApiModelProperty(value = "状态码描述(业务定义)", required = true, dataType = "string")
  private String message = "操作成功";

  /**
   * 
   * 描述 : 结果集(泛型)
   * 
   */
  @ApiModelProperty(value = "结果集(泛型)", required = true, dataType = "object")
  private T result = null; //NOSONAR

  /**
   * 描述 : 错误
   */
  @ApiModelProperty(value = "错误", required = true, dataType = "object")
  private ErrorResult error = null;

  /**
   * 
   * 描述 : 构造函数
   *
   * 
   * 
   */
  public RestResponse() {
    super();
  }

  /**
   * 
   * 描述 : 构造函数
   *
   * 
   * 
   * @param result 结果集(泛型)
   * 
   */
  public RestResponse(T result) {
    super();
    this.result = result;
  }

  /**
   * 
   * 描述 : 构造函数
   * 
   * @param errorCode 错误码
   * 
   * @param error 错误
   * 
   */
  public RestResponse(ErrorCode errorCode, ErrorResult error) {
    super();
    this.code = errorCode.name();
    this.message = errorCode.value();
    this.error = error;
  }

  /**
   * 
   * 描述 : 构造函数
   * 
   * @param code 状态码(业务定义)
   * 
   * @param message 状态码描述(业务定义)
   * 
   * @param error 错误
   * 
   */
  public RestResponse(String code, String message, ErrorResult error) {
    super();
    this.code = code;
    this.message = message;
    this.error = error;
  }

  /**
   * 
   * 描述 : 构造函数
   * 
   * @param code 状态码(业务定义)
   * 
   * @param message 状态码描述(业务定义)
   * 
   * 
   */
  public RestResponse(String code, String message) {
    super();
    this.code = code;
    this.message = message;
  }

  /**
   * 
   * 描述 : 构造函数
   *
   * 
   * 
   * @param code 状态码(业务定义)
   * 
   * @param message 状态码描述(业务定义)
   * 
   * @param result 结果集(泛型)
   * 
   */
  public RestResponse(String code, String message, T result) {
    super();
    this.code = code;
    this.message = message;
    this.result = result;
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
   * 描述 : 获取code
   *
   * @return the code
   */
  public String getCode() {
    return code;
  }

  /**
   * 描述 : 设置code
   *
   * @param code the code to set
   */
  public void setCode(String code) {
    this.code = code;
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
   * 描述 : 获取result
   *
   * @return the result
   */
  public T getResult() {
    return result;
  }

  /**
   * 描述 : 设置result
   *
   * @param result the result to set
   */
  public void setResult(T result) {
    this.result = result;
  }

  /**
   * 描述 : 获取error
   *
   * @return the error
   */
  public ErrorResult getError() {
    return error;
  }

  /**
   * 描述 : 设置error
   *
   * @param error the error to set
   */
  public void setError(ErrorResult error) {
    this.error = error;
  }

}
