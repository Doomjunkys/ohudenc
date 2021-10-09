/**
 * FileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file.web;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.egridcloud.udf.core.ApplicationConfig;
import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.file.FileProperties;
import com.egridcloud.udf.file.domain.FileInfo;
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
   * 描述 : applicationConfig
   */
  @Autowired
  private ApplicationConfig applicationConfig;

  /**
   * 描述 : fileProperties
   */
  @Autowired
  private FileProperties fileProperties;

  /**
   * 描述 : fileService
   */
  @Autowired
  private FileService fileService;

  @Override
  public RestResponse<FileInfo> upload(String pathCode, MultipartFile file)
      throws IllegalStateException, IOException {
    return new RestResponse<>(fileService.upload(pathCode, file));
  }

  @Override
  public RestResponse<FileProperties> properties() {
    return new RestResponse<>(fileProperties);
  }

  @Override
  public void load(@PathVariable String fileId, HttpServletResponse response) throws IOException {
    this.loadFile(fileId, response);
  }

  @Override
  public RestResponse<FileInfo> info(@PathVariable String fileId) throws IOException {
    return new RestResponse<>(this.fileService.getFileInfo(fileId));
  }

  /**
   * 描述 : 加载文件
   *
   * @param fileId 相对路径
   * @param response 响应对象
   * @throws IOException IOException
   */
  private void loadFile(@PathVariable String fileId, HttpServletResponse response)
      throws IOException {
    //获得文件信息
    FileInfo fileInfo = this.fileService.getFileInfo(fileId);
    //设置response
    response.setContentLengthLong(fileInfo.getSize());
    response.setCharacterEncoding(applicationConfig.getEncoding());
    response.setContentType(fileInfo.getContentType());
    //获得rootPath
    String rootPath = fileProperties.getRootPath();
    //拼接绝对路径(目录)
    String absolutePath = rootPath + fileInfo.getRelativePath();
    //获得资源对象
    FileSystemResource fsr = new FileSystemResource(absolutePath);
    //输出文件
    final int buffInt = 1024;
    byte[] buff = new byte[buffInt];
    OutputStream os = null;
    BufferedInputStream bis = null;
    try { //NOSONAR
      os = response.getOutputStream();
      bis = new BufferedInputStream(fsr.getInputStream());
      int i = bis.read(buff);
      while (i != -1) {
        os.write(buff, 0, buff.length);
        os.flush();
        i = bis.read(buff);
      }
    } finally {
      if (bis != null) {
        bis.close();
      }
      if (bis != null) {
        bis.close();
      }
    }
  }

}
