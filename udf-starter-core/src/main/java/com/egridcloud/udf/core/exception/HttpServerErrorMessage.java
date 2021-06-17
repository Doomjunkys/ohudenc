/**
 * HttpServerErrorMessage.java
 * Created at 2016-11-05
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core.exception;

import java.io.IOException;
import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;

import com.egridcloud.udf.core.RestResponse;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 描述 : HttpServerErrorMessage
 *
 * @author wangkang
 *
 */
public class HttpServerErrorMessage implements Serializable {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerErrorMessage.class);

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : rawStatusCode
   */
  private int rawStatusCode;

  /**
   * 描述 : statusText
   */
  private String statusText;

  /**
   * 描述 : responseBody
   */
  private RestResponse<ErrorMessage> responseBody;

  /**
   * 描述 : responseHeaders
   */
  private HttpHeaders responseHeaders;

  /**
   * 描述 : statusCode
   */
  private HttpStatus statusCode;

  /**
   * 描述 : 构造函数
   *
   * @param httpServerErrorException httpServerErrorException
   * @param xssObjectMapper xssObjectMapper
   */
  public HttpServerErrorMessage(HttpServerErrorException httpServerErrorException,
      ObjectMapper xssObjectMapper) {
    rawStatusCode = httpServerErrorException.getRawStatusCode();
    responseHeaders = httpServerErrorException.getResponseHeaders();
    statusCode = httpServerErrorException.getStatusCode();
    statusText = httpServerErrorException.getStatusText();
    try {
      JavaType javaType = xssObjectMapper.getTypeFactory()
          .constructParametricType(RestResponse.class, ErrorMessage.class);
      responseBody =
          xssObjectMapper.readValue(httpServerErrorException.getResponseBodyAsString(), javaType);
    } catch (JsonParseException e) {
      LOGGER.error("JsonParseException:", e);
    } catch (JsonMappingException e1) {
      LOGGER.error("JsonMappingException:", e1);
    } catch (IOException e2) {
      LOGGER.error("IOException:", e2);
    }
  }

  /**
   * 描述 : 获取responseBody
   *
   * @return the responseBody
   */
  public RestResponse<ErrorMessage> getResponseBody() {
    return responseBody;
  }

  /**
   * 描述 : 设置responseBody
   *
   * @param responseBody the responseBody to set
   */
  public void setResponseBody(RestResponse<ErrorMessage> responseBody) {
    this.responseBody = responseBody;
  }

  /**
   * 描述 : 获取rawStatusCode
   *
   * @return the rawStatusCode
   */
  public int getRawStatusCode() {
    return rawStatusCode;
  }

  /**
   * 描述 : 设置rawStatusCode
   *
   * @param rawStatusCode the rawStatusCode to set
   */
  public void setRawStatusCode(int rawStatusCode) {
    this.rawStatusCode = rawStatusCode;
  }

  /**
   * 描述 : 获取statusText
   *
   * @return the statusText
   */
  public String getStatusText() {
    return statusText;
  }

  /**
   * 描述 : 设置statusText
   *
   * @param statusText the statusText to set
   */
  public void setStatusText(String statusText) {
    this.statusText = statusText;
  }

  /**
   * 描述 : 获取responseHeaders
   *
   * @return the responseHeaders
   */
  public HttpHeaders getResponseHeaders() {
    return responseHeaders;
  }

  /**
   * 描述 : 设置responseHeaders
   *
   * @param responseHeaders the responseHeaders to set
   */
  public void setResponseHeaders(HttpHeaders responseHeaders) {
    this.responseHeaders = responseHeaders;
  }

  /**
   * 描述 : 获取statusCode
   *
   * @return the statusCode
   */
  public HttpStatus getStatusCode() {
    return statusCode;
  }

  /**
   * 描述 : 设置statusCode
   *
   * @param statusCode the statusCode to set
   */
  public void setStatusCode(HttpStatus statusCode) {
    this.statusCode = statusCode;
  }

}
