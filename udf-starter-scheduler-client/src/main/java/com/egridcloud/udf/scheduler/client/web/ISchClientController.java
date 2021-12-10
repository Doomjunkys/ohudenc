/**
 * ISchClientController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobParam;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述 : ISchClientController
 *
 * @author Administrator
 *
 */
@Api(value = "计划任务客户端", consumes = "application/json", produces = "application/json",
    protocols = "http")
@RequestMapping(value = "/service/scheduler/client")
public interface ISchClientController {

  /**
   * 描述 : 执行计划任务
   *
   * @param param param
   *
   * @return 执行计划任务
   */
  @ApiOperation(value = "接口编号自定义", notes = "执行计划任务")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string") })
  @RequestMapping(method = RequestMethod.POST)
  public RestResponse<GeneralJobResult> execute(@RequestBody GeneralJobParam param);

}
