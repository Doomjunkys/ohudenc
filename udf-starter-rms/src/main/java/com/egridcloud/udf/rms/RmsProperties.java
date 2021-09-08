/**
 * Config.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import java.io.Serializable;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.rms.mate.ApplicationMate;
import com.egridcloud.udf.rms.mate.ServiceMate;

/**
 * 描述 : Config
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.rms.properties")
public class RmsProperties implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 应用清单(应用名称 : 应用地址)
   */
  private Map<String, ApplicationMate> application;

  /**
   * 描述 : 服务路径(服务编号 : 服务元数据)
   */
  private Map<String, ServiceMate> service;

  /**
   * 描述 : 获取application
   *
   * @return the application
   */
  public Map<String, ApplicationMate> getApplication() {
    return application;
  }

  /**
   * 描述 : 设置application
   *
   * @param application the application to set
   */
  public void setApplication(Map<String, ApplicationMate> application) {
    this.application = application;
  }

  /**
   * 描述 : 获取service
   *
   * @return the service
   */
  public Map<String, ServiceMate> getService() {
    return service;
  }

  /**
   * 描述 : 设置service
   *
   * @param service the service to set
   */
  public void setService(Map<String, ServiceMate> service) {
    this.service = service;
  }

}
