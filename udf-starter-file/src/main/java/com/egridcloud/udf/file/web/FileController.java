/**
 * FileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.file.service.FileService;

/**
 * 描述 : FileController
 *
 * @author Administrator
 *
 */
@RestController
public class FileController implements IFileController {

  /**
   * 描述 : fileService
   */
  @Autowired
  private FileService fileService;

  @Override
  public RestResponse<String> upload(String pathCode, Boolean cover, MultipartFile file) {
    return new RestResponse<>(pathCode);
  }

}
