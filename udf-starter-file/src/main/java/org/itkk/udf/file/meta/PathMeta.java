/**
 * PathMeta.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file.meta;

import java.io.Serializable;

/**
 * 描述 : PathMeta
 *
 * @author Administrator
 *
 */
public class PathMeta implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 应用名称
   */
  private String owner;

  /**
   * 描述 : 文件路径
   */
  private String path;

  /**
   * 描述 : 描述
   */
  private String description;

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
   * 描述 : 获取path
   *
   * @return the path
   */
  public String getPath() {
    return path;
  }

  /**
   * 描述 : 设置path
   *
   * @param path the path to set
   */
  public void setPath(String path) {
    this.path = path;
  }

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

}
