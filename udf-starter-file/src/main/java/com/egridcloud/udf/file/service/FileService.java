/**
 * FileService.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.egridcloud.udf.file.FileException;
import com.egridcloud.udf.file.FileProperties;

/**
 * 描述 : FileService
 *
 * @author Administrator
 *
 */
@Service
public class FileService {

  /**
   * 描述 : fileProperties
   */
  @Autowired
  private FileProperties fileProperties;

  /**
   * 描述 : 验证
   *
   * @param pathCode 路径代码
   * @param file 文件
   */
  public void verification(String pathCode, MultipartFile file) {
    //验证pathCode是否存在
    if (!fileProperties.getPath().containsKey(pathCode)) {
      throw new FileException("pathCode : " + pathCode + " not defined");
    }
    //文件是否为空
    if (file.isEmpty()) {
      throw new FileException("file is empty");
    }
  }

}
