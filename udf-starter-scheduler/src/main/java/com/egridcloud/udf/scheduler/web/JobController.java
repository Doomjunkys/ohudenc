/**
 * JobController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.web;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.scheduler.service.JobService;

/**
 * 描述 : JobController
 *
 * @author Administrator
 *
 */
@RestController
public class JobController implements IJobController {

  /**
   * 描述 : jobService
   */
  @Autowired
  private JobService jobService;

  @Override
  public RestResponse<String> save(@PathVariable String jobCode)
      throws ClassNotFoundException, SchedulerException {
    jobService.save(jobCode, false);
    return new RestResponse<>();
  }

  @Override
  public RestResponse<String> saveCover(@PathVariable String jobCode)
      throws ClassNotFoundException, SchedulerException {
    jobService.save(jobCode, true);
    return new RestResponse<>();
  }

  @Override
  public RestResponse<String> remove(@PathVariable String jobCode) throws SchedulerException {
    jobService.remove(jobCode);
    return new RestResponse<>();
  }

}
