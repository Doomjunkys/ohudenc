/**
 * IFileListener.java
 * Created at 2017-05-28
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file;

import com.egridcloud.udf.file.domain.FileInfo;

/**
 * 描述 : IFileListener
 *
 * @author Administrator
 *
 */
public interface IFileListener {

  /**
   * 描述 : 已经上传
   *
   * @param fileInfo 文件信息
   */
  public void uploaded(FileInfo fileInfo);

}
