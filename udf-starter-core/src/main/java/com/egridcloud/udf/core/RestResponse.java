/**
 * RestResponse.java
 * Created at 2016-09-19
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core;

import java.io.Serializable;

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
   * 
   * 描述 : 默认状态码
   * 
   */
  private static final String DEF_RETURN_CODE = "SUCCESS";

  /**
   * 
   * 描述 : 默认状态码描述
   * 
   */
  private static final String DEF_RETURN_MESSAGE = "操作成功";

  /**
   * 
   * 描述 : 状态码(业务定义)
   * 
   */
  @ApiModelProperty(value = "状态码(业务定义)", required = true, dataType = "string")
  private String returnCode = DEF_RETURN_CODE;

  /**
   * 
   * 描述 : 状态码描述(业务定义)
   * 
   */
  @ApiModelProperty(value = "状态码描述(业务定义)", required = true, dataType = "string")
  private String returnMessage = DEF_RETURN_MESSAGE;

  /**
   * 
   * 描述 : 结果集(泛型)
   * 
   */
  @ApiModelProperty(value = "结果集(泛型)", required = true, dataType = "object")
  private T result; //NOSONAR

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
   * 
   * 
   * @param returnCode 状态码(业务定义)
   * 
   * @param returnMessage 状态码描述(业务定义)
   * 
   * @param result 结果集(泛型)
   * 
   */
  public RestResponse(String returnCode, String returnMessage, T result) {
    super();
    this.returnCode = returnCode;
    this.returnMessage = returnMessage;
    this.result = result;
  }

  /**
   * 
   * 描述 : 获取returnCode
   *
   * 
   * 
   * @return the returnCode
   * 
   */
  public String getReturnCode() {
    return returnCode;
  }

  /**
   * 
   * 描述 : 设置returnCode
   *
   * 
   * 
   * @param returnCode the returnCode to set
   * 
   */
  public void setReturnCode(String returnCode) {
    this.returnCode = returnCode;
  }

  /**
   * 
   * 描述 : 获取returnMessage
   *
   * 
   * 
   * @return the returnMessage
   * 
   */
  public String getReturnMessage() {
    return returnMessage;
  }

  /**
   * 
   * 描述 : 设置returnMessage
   *
   * 
   * 
   * @param returnMessage the returnMessage to set
   * 
   */
  public void setReturnMessage(String returnMessage) {
    this.returnMessage = returnMessage;
  }

  /**
   * 
   * 描述 : 获取result
   *
   * 
   * 
   * @return the result
   * 
   */
  public T getResult() {
    return result;
  }

  /**
   * 
   * 描述 : 设置result
   *
   * 
   * 
   * @param result the result to set
   * 
   */
  public void setResult(T result) {
    this.result = result;
  }

}
