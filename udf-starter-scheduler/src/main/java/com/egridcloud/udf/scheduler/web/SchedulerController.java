/**
 * SchedulerController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.scheduler.web;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.scheduler.service.SchedulerService;

/**
 * 描述 : SchedulerController
 *
 * @author Administrator
 *
 */
@RestController
public class SchedulerController implements ISchedulerController {

  /**
   * 描述 : SchedulerService
   */
  @Autowired
  private SchedulerService schedulerService;

  @Override
  public RestResponse<String> clear() throws SchedulerException {
    schedulerService.clear();
    return new RestResponse<>();
  }

}
