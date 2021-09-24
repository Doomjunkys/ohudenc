/**
 * Config.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file;

import java.io.Serializable;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import com.egridcloud.udf.file.mate.PathMate;

/**
 * 描述 : Config
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.file.properties")
@Validated
public class FileProperties implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 根路径
   */
  @NotNull
  private String rootPath;

  /**
   * 描述 : 文件路径 ( key : pathCode )
   */
  private Map<String, PathMate> path;

  /**
   * 描述 : 获取rootPath
   *
   * @return the rootPath
   */
  public String getRootPath() {
    return rootPath;
  }

  /**
   * 描述 : 设置rootPath
   *
   * @param rootPath the rootPath to set
   */
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  /**
   * 描述 : 获取path
   *
   * @return the path
   */
  public Map<String, PathMate> getPath() {
    return path;
  }

  /**
   * 描述 : 设置path
   *
   * @param path the path to set
   */
  public void setPath(Map<String, PathMate> path) {
    this.path = path;
  }

}
