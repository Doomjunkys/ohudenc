/**
 * ISchClientCallbackController.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.scheduler.client.domain.RmsJobResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述 : ISchClientCallbackController
 *
 * @author Administrator
 *
 */
@Api(value = "调度客户端回调服务", consumes = "application/json", produces = "application/json",
    protocols = "http")
@RequestMapping(value = "/service/scheduler/client/callback")
public interface ISchClientCallbackController {

  /**
   * 描述 : 客户端回调
   *
   * @param result 执行结果
   * @return 结果
   */
  @ApiOperation(value = "SCH_CLIENT_CALLBACK_1", notes = "客户端回调")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string") })
  @RequestMapping(method = RequestMethod.POST)
  public RestResponse<String> callback(@RequestBody RmsJobResult result);

}
