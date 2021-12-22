/**
 * IJobController.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.web;

import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.egridcloud.udf.core.RestResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 描述 : IJobController
 *
 * @author Administrator
 *
 */
@Api(value = "调度服务", consumes = "application/json", produces = "application/json",
    protocols = "http")
@RequestMapping(value = "/service/scheduler")
public interface ISchedulerController {

  /**
   * 描述 : 清理数据
   *
   * @return 操作结果
   * @throws SchedulerException SchedulerException
   */
  @ApiOperation(value = "SCHEDULER_1", notes = "清理数据")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string") })
  @RequestMapping(value = "clear", method = RequestMethod.DELETE)
  public RestResponse<String> clear() throws SchedulerException;

}
