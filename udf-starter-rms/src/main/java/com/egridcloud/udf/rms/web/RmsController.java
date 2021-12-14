/**
 * RmsController.java
 * Created at 2017-05-25
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.rms.RmsConfig;
import com.egridcloud.udf.rms.RmsProperties;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * 描述 : RmsController
 *
 * @author Administrator
 *
 */
@Api(value = "rest服务", consumes = "application/json", produces = "application/json",
    protocols = "http")
@RestController
@RequestMapping("rms")
public class RmsController {

  /**
   * 描述 : rmsConfig
   */
  @Autowired
  private RmsConfig rmsConfig;

  /**
   * 描述 : rmsProperties
   */
  @Autowired(required = false)
  private RmsProperties rmsProperties;

  /**
   * 描述 : 返回rms扫描路径
   *
   * @return rms扫描路径
   */
  @ApiOperation(value = "返回rms扫描路径", notes = "返回rms扫描路径")
  @RequestMapping(value = "path/pattern", method = RequestMethod.GET)
  public RestResponse<String> getPathPatterns() {
    return new RestResponse<>(rmsConfig.getRmsPathPatterns());
  }

  /**
   * 描述 : 获得rms配置详情
   *
   * @return rms配置详情
   */
  @ApiOperation(value = "获得rms配置详情", notes = "获得rms配置详情")
  @RequestMapping(value = "properties", method = RequestMethod.GET)
  public RestResponse<RmsProperties> getProperties() {
    return new RestResponse<>(rmsProperties);
  }

}
