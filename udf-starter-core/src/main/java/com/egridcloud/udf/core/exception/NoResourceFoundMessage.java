/**
 * NoResourceFoundMessage.java
 * Created at 2016-11-05
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

import java.io.Serializable;

import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 描述 : NoResourceFoundMessage
 *
 * @author wangkang
 *
 */
public class NoResourceFoundMessage implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : httpMethod
   */
  private String httpMethod;

  /**
   * 描述 : requestURL
   */
  private String requestUrl;

  /**
   * 描述 : headers
   */
  private HttpHeaders headers;

  /**
   * 描述 : 构造函数
   *
   * @param noHandlerFoundException noHandlerFoundException
   */
  public NoResourceFoundMessage(NoHandlerFoundException noHandlerFoundException) {
    this.headers = noHandlerFoundException.getHeaders();
    this.httpMethod = noHandlerFoundException.getHttpMethod();
    this.requestUrl = noHandlerFoundException.getRequestURL();
  }

  /**
   * 描述 : 获取httpMethod
   *
   * @return the httpMethod
   */
  public String getHttpMethod() {
    return httpMethod;
  }

  /**
   * 描述 : 设置httpMethod
   *
   * @param httpMethod the httpMethod to set
   */
  public void setHttpMethod(String httpMethod) {
    this.httpMethod = httpMethod;
  }

  /**
   * 描述 : 获取requestUrl
   *
   * @return the requestUrl
   */
  public String getRequestUrl() {
    return requestUrl;
  }

  /**
   * 描述 : 设置requestUrl
   *
   * @param requestUrl the requestUrl to set
   */
  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  /**
   * 描述 : 获取headers
   *
   * @return the headers
   */
  public HttpHeaders getHeaders() {
    return headers;
  }

  /**
   * 描述 : 设置headers
   *
   * @param headers the headers to set
   */
  public void setHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

}
