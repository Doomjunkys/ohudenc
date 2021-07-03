/**
 * AuthMate.java
 * Created at 2016-12-22
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms.mate;

/**
 * 描述 : Auth元数据
 *
 * @author Administrator
 *
 */
public class AuthMate {

  /**
   * 描述 : 私钥
   */
  private String secret;

  /**
   * 描述 : 权限
   */
  private String purview;

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
