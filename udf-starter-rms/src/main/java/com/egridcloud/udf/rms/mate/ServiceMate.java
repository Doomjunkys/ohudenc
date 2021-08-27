/**
 * ServiceMate.java
 * Created at 2016-12-22
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms.mate;

/**
 * 描述 : service元数据
 *
 * @author Administrator
 *
 */
public class ServiceMate {

  /**
   * 描述 : 应用名称
   */
  private String applicationName;

  /**
   * 描述 : 地址
   */
  private String uri;

  /**
   * 描述 : 服务方法
   */
  private String method;

  /**
   * 描述 : 是否HTTPS
   */
  private Boolean isHttps = false;

  /**
   * 描述 : 获取isHttps
   *
   * @return the isHttps
   */
  public Boolean getIsHttps() {
    return isHttps;
  }

  /**
   * 描述 : 设置isHttps
   *
   * @param isHttps the isHttps to set
   */
  public void setIsHttps(Boolean isHttps) {
    this.isHttps = isHttps;
  }

  /**
   * 描述 : 获取applicationName
   *
   * @return the applicationName
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * 描述 : 设置applicationName
   *
   * @param applicationName the applicationName to set
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * 描述 : 获取uri
   *
   * @return the uri
   */
  public String getUri() {
    return uri;
  }

  /**
   * 描述 : 设置uri
   *
   * @param uri the uri to set
   */
  public void setUri(String uri) {
    this.uri = uri;
  }

  /**
   * 描述 : 获取method
   *
   * @return the method
   */
  public String getMethod() {
    return method;
  }

  /**
   * 描述 : 设置method
   *
   * @param method the method to set
   */
  public void setMethod(String method) {
    this.method = method;
  }

}
