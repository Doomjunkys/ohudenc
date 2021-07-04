/**
 * Config.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.rms.mate.AuthMate;
import com.egridcloud.udf.rms.mate.PathMate;

/**
 * 描述 : Config
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.rms.config")
public class RmsProperties {

  /**
   * 描述 : 应用清单(应用名称 : 应用地址)
   */
  private Map<String, String> application;

  /**
   * 描述 : 服务路径(服务编号 : 服务元数据)
   */
  private Map<String, PathMate> service;

  /**
   * 描述 : 认证(应用名称 : 权限元数据)
   */
  private Map<String, AuthMate> authorization;

  /**
   * 描述 : 获取application
   *
   * @return the application
   */
  public Map<String, String> getApplication() {
    return application;
  }

  /**
   * 描述 : 设置application
   *
   * @param application the application to set
   */
  public void setApplication(Map<String, String> application) {
    this.application = application;
  }

  /**
   * 描述 : 获取service
   *
   * @return the service
   */
  public Map<String, PathMate> getService() {
    return service;
  }

  /**
   * 描述 : 设置service
   *
   * @param service the service to set
   */
  public void setService(Map<String, PathMate> service) {
    this.service = service;
  }

  /**
   * 描述 : 获取authorization
   *
   * @return the authorization
   */
  public Map<String, AuthMate> getAuthorization() {
    return authorization;
  }

  /**
   * 描述 : 设置authorization
   *
   * @param authorization the authorization to set
   */
  public void setAuthorization(Map<String, AuthMate> authorization) {
    this.authorization = authorization;
  }

}
