/**
 * ApplicationConfig.java
 * Created at 2016-10-03
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 描述 : 系统配置
 *
 * @author wangkang
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.application.config")
@Validated
public class ApplicationConfig {

  /**
   * 描述 : 是否输出异常堆栈
   */
  @NotNull
  private boolean outputExceptionStackTrace = false;

  /**
   * 描述 : 编码
   */
  @NotNull
  private String encoding = "UTF-8";

  /**
   * 描述 : 时区
   */
  @NotNull
  private String timeZone = "Asia/Shanghai";

  /**
   * 描述 : 日期格式
   */
  @NotNull
  private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss:SSSZ";

  /**
   * 描述 : 获取timeZone
   *
   * @return the timeZone
   */
  public String getTimeZone() {
    return timeZone;
  }

  /**
   * 描述 : 设置timeZone
   *
   * @param timeZone the timeZone to set
   */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * 描述 : 获取encoding
   *
   * @return the encoding
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * 描述 : 设置encoding
   *
   * @param encoding the encoding to set
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * 描述 : 获取dateFormat
   *
   * @return the dateFormat
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * 描述 : 设置dateFormat
   *
   * @param dateFormat the dateFormat to set
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * 描述 : 获取outputExceptionStackTrace
   *
   * @return the outputExceptionStackTrace
   */
  public boolean isOutputExceptionStackTrace() {
    return outputExceptionStackTrace;
  }

  /**
   * 描述 : 设置outputExceptionStackTrace
   *
   * @param outputExceptionStackTrace the outputExceptionStackTrace to set
   */
  public void setOutputExceptionStackTrace(boolean outputExceptionStackTrace) {
    this.outputExceptionStackTrace = outputExceptionStackTrace;
  }

}
