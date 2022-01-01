/**
 * ServiceMeta.java
 * Created at 2016-12-22
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.rms.meta;

import java.io.Serializable;

/**
 * 描述 : ServiceMeta
 *
 * @author Administrator
 *
 */
public class ServiceMeta implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 应用名称
   */
  private String owner;

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
   * 描述 : 描述
   */
  private String description;

  /**
   * 描述 : 获取description
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * 描述 : 设置description
   *
   * @param description the description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }

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
   * 描述 : 获取owner
   *
   * @return the owner
   */
  public String getOwner() {
    return owner;
  }

  /**
   * 描述 : 设置owner
   *
   * @param owner the owner to set
   */
  public void setOwner(String owner) {
    this.owner = owner;
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
