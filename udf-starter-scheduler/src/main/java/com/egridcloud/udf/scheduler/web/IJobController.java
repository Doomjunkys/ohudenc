/**
 * IJobController.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.scheduler.web;

import java.util.Map;

import org.quartz.SchedulerException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
@Api(value = "作业服务", consumes = "application/json", produces = "application/json",
    protocols = "http")
@RequestMapping(value = "/service/scheduler/job")
public interface IJobController {

  /**
   * 描述 : 保存作业
   *
   * @param jobCode 作业代码
   * @return 操作结果
   * @throws ClassNotFoundException ClassNotFoundException
   * @throws SchedulerException SchedulerException
   */
  @ApiOperation(value = "SCHEDULER_JOB_1", notes = "保存作业")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "path", name = "jobCode", value = "作业代码", required = true,
          dataType = "string") })
  @RequestMapping(value = "{jobCode}", method = RequestMethod.PUT)
  public RestResponse<String> save(@PathVariable String jobCode) //NOSONAR
      throws ClassNotFoundException, SchedulerException;

  /**
   * 描述 : 保存作业(覆盖)
   *
   * @param jobCode 作业代码
   * @return 操作结果
   * @throws ClassNotFoundException ClassNotFoundException
   * @throws SchedulerException SchedulerException
   */
  @ApiOperation(value = "SCHEDULER_JOB_2", notes = "保存作业(覆盖)")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "path", name = "jobCode", value = "作业代码", required = true,
          dataType = "string") })
  @RequestMapping(value = "{jobCode}/cover", method = RequestMethod.PUT)
  public RestResponse<String> saveCover(@PathVariable String jobCode) //NOSONAR
      throws ClassNotFoundException, SchedulerException;

  /**
   * 描述 : 移除作业
   *
   * @param jobCode 作业代码
   * @return 操作结果
   * @throws SchedulerException SchedulerException
   */
  @ApiOperation(value = "SCHEDULER_JOB_3", notes = "移除作业")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "path", name = "jobCode", value = "作业代码", required = true,
          dataType = "string") })
  @RequestMapping(value = "{jobCode}", method = RequestMethod.DELETE)
  public RestResponse<String> remove(@PathVariable String jobCode) //NOSONAR
      throws SchedulerException;

  /**
   * 描述 : 触发作业
   *
   * @param jobCode 作业代码
   * @param jobDataMap jobDataMap
   * @return 操作结果
   * @throws SchedulerException SchedulerException
   */
  @ApiOperation(value = "SCHEDULER_JOB_4", notes = "触发作业")
  @ApiImplicitParams({
      @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true,
          dataType = "string"),
      @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号",
          required = true, dataType = "string"),
      @ApiImplicitParam(paramType = "path", name = "jobCode", value = "作业代码", required = true,
          dataType = "string") })
  @RequestMapping(value = "trigger/{jobCode}", method = RequestMethod.POST)
  public RestResponse<String> trigger(@PathVariable String jobCode,
      @RequestBody Map<String, Object> jobDataMap) throws SchedulerException;

}
