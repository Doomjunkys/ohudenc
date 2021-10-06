/**
 * FileService.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file.service;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.egridcloud.udf.core.ApplicationConfig;
import com.egridcloud.udf.file.FileException;
import com.egridcloud.udf.file.FileProperties;
import com.egridcloud.udf.file.IFileListener;
import com.egridcloud.udf.file.domain.FileInfo;

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
   * 描述 : applicationConfig
   */
  @Autowired
  private ApplicationConfig applicationConfig;

  /**
   * 描述 : fileListener
   */
  @Autowired(required = false)
  private IFileListener fileListener;

  /**
   * 描述 : 上传文件
   *
   * @param pathCode 路径代码
   * @param file 文件
   * @return 结果
   * @throws IllegalStateException 异常
   * @throws IOException 异常
   */
  public FileInfo upload(String pathCode, MultipartFile file)
      throws IllegalStateException, IOException {
    //验证
    this.verification(pathCode, file);
    //创建返回对象
    FileInfo fileInfo = new FileInfo();
    //获得rootPath
    String rootPath = fileProperties.getRootPath();
    //根据pathCode获得path
    String path = fileProperties.getPath().get(pathCode).getPath();
    //获得当前时间戳
    String day = new SimpleDateFormat("yyyyMMdd").format(new Date()) + "/";
    //获得文件名
    String originalFilename = file.getOriginalFilename();
    fileInfo.setName(originalFilename);
    //获得上下文类型
    String contentType = file.getContentType();
    fileInfo.setContentType(contentType);
    //获得文件长度
    Long size = file.getSize();
    fileInfo.setSize(size);
    //拼接相对路径(目录)
    String relativePath = path + day;
    //拼接相对路径(文件)
    String relativeFilePath = relativePath + originalFilename;
    fileInfo.setRelativePath(relativeFilePath);
    //拼接绝对路径(目录)
    String absolutePath = rootPath + relativePath;
    //拼接绝对路径(文件)
    String absoluteFilePath = rootPath + relativeFilePath;
    //获得base64文件ID(通过文件相对路径计算获得)
    String fileId = new String(
        Base64.encodeBase64(fileInfo.getRelativePath().getBytes(applicationConfig.getEncoding())));
    //使用url转码处理特殊字符
    fileId = URLEncoder.encode(fileId, applicationConfig.getEncoding());
    fileInfo.setId(fileId);
    //获得文件对象(目录)
    File dest = new File(absolutePath);
    //创建目录
    dest.mkdirs();
    //获得文件对象
    dest = new File(absoluteFilePath);
    //保存
    file.transferTo(dest);
    //触发监听器
    if (fileListener != null) {
      fileListener.uploaded(fileInfo);
    }
    //返回
    return fileInfo;
  }

  /**
   * 描述 : 验证
   *
   * @param pathCode 路径代码
   * @param file 文件
   */
  public void verification(String pathCode, MultipartFile file) {
    //验证根路径是否配置
    if (fileProperties.getRootPath() == null) {
      throw new FileException("rootPath not defined");
    }
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
