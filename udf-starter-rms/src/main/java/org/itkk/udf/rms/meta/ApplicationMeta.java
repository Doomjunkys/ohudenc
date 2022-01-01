/**
 * ApplicationMeta.java
 * Created at 2016-12-22
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.rms.meta;

import java.io.Serializable;

/**
 * 描述 : ApplicationMeta
 *
 * @author Administrator
 *
 */
public class ApplicationMeta implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 服务ID
   */
  private String serviceId;

  /**
   * 描述 : 私钥
   */
  private String secret;

  /**
   * 描述 : 权限
   */
  private String purview;

  /**
   * 描述 : 所有服务的调用权限(优先判定)
   */
  private Boolean all = false;

  /**
   * 描述 : 禁止服务调用
   */
  private Boolean disabled = false;

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
   * 描述 : 获取serviceId
   *
   * @return the serviceId
   */
  public String getServiceId() {
    return serviceId;
  }

  /**
   * 描述 : 设置serviceId
   *
   * @param serviceId the serviceId to set
   */
  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  /**
   * 描述 : 获取all
   *
   * @return the all
   */
  public Boolean getAll() {
    return all;
  }

  /**
   * 描述 : 设置all
   *
   * @param all the all to set
   */
  public void setAll(Boolean all) {
    this.all = all;
  }

  /**
   * 描述 : 获取disabled
   *
   * @return the disabled
   */
  public Boolean getDisabled() {
    return disabled;
  }

  /**
   * 描述 : 设置disabled
   *
   * @param disabled the disabled to set
   */
  public void setDisabled(Boolean disabled) {
    this.disabled = disabled;
  }

  /**
   * 描述 : 获取secret
   *
   * @return the secret
   */
  public String getSecret() {
    return secret;
  }

  /**
   * 描述 : 设置secret
   *
   * @param secret the secret to set
   */
  public void setSecret(String secret) {
    this.secret = secret;
  }

  /**
   * 描述 : 获取purview
   *
   * @return the purview
   */
  public String getPurview() {
    return purview;
  }

  /**
   * 描述 : 设置purview
   *
   * @param purview the purview to set
   */
  public void setPurview(String purview) {
    this.purview = purview;
  }

}
