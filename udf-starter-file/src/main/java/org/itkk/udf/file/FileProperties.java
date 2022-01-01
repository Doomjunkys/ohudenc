/**
 * Config.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file;

import java.io.Serializable;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import org.itkk.udf.file.meta.PathMeta;

/**
 * 描述 : Config
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "org.itkk.file.properties")
public class FileProperties implements Serializable {

  /**
   * 描述 : ID
   */
  private static final long serialVersionUID = 1L;

  /**
   * 描述 : 根路径
   */
  private String rootPath;

  /**
   * 描述 : 文件路径 ( key : pathCode )
   */
  private Map<String, PathMeta> path;

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
  public Map<String, PathMeta> getPath() {
    return path;
  }

  /**
   * 描述 : 设置path
   *
   * @param path the path to set
   */
  public void setPath(Map<String, PathMeta> path) {
    this.path = path;
  }

}
