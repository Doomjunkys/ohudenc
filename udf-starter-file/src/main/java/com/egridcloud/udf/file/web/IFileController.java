/**
 * IFileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.file.web;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.core.domain.file.FileInfo;
import com.egridcloud.udf.core.domain.file.FileParam;
import com.egridcloud.udf.file.FileProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述 : IFileController
 *
 * @author Administrator
 *
 */
@Api(value = "文件服务", consumes = "application/json", produces = "application/json",
    protocols = "http")
@RequestMapping(value = "/service/file")
public interface IFileController {

  /**
   * 描述 : 文件配置
   *
   * @return 文件配置
   */
  @ApiOperation(value = "FILE_2", notes = "文件配置")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string") })
  @RequestMapping(value = "properties", method = RequestMethod.GET)
  public RestResponse<FileProperties> properties();

  /**
   * 描述 : 上传文件
   *
   * @param pathCode 路径代码
   * @param file 文件
   * @return 结果
   * @throws IllegalStateException 异常
   * @throws IOException 异常
   */
  @ApiOperation(value = "FILE_1", notes = "上传文件")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "form", name = "pathCode", value = "路径代码", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "form", name = "file", value = "文件", required = true,
          dataType = "__file") })
  @RequestMapping(value = "upload", method = RequestMethod.POST)
  public RestResponse<FileInfo> upload(String pathCode, MultipartFile file)
      throws IllegalStateException, IOException;

  /**
   * 描述 : 通过文件ID访问文件
   *
   * @param fileParam 文件参数
   * @param response 响应对象
   * @throws IOException 异常
   */
  @ApiOperation(value = "FILE_3", notes = "通过文件ID访问文件")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string") })
  @RequestMapping(value = "download", method = RequestMethod.POST)
  public void download(@RequestBody FileParam fileParam, HttpServletResponse response)
      throws IOException;

  /**
   * 描述 : 通过文件ID访问文件
   *
   * @param fileParam 文件参数
   * @return 文件信息
   * @throws IOException 异常
   */
  @ApiOperation(value = "FILE_4", notes = "通过文件ID获得文件信息")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string") })
  @RequestMapping(value = "info", method = RequestMethod.POST)
  public RestResponse<FileInfo> info(@RequestBody FileParam fileParam) throws IOException;

}
