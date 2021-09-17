/**
 * RabbitmqController.java
 * Created at 2017-05-25
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.amqp.rabbitmq.RabbitmqProperties;
import com.egridcloud.udf.core.RestResponse;

import springfox.documentation.annotations.ApiIgnore;

/**
 * 描述 : RabbitmqController
 *
 * @author Administrator
 *
 */
@ApiIgnore
@RestController
@RequestMapping("rabbitmq")
public class RabbitmqController {

  /**
   * 描述 : rabbitmqProperties
   */
  @Autowired(required = false)
  private RabbitmqProperties rabbitmqProperties;

  /**
   * 描述 : 返回rabbitmq配置
   *
   * @return rabbitmq配置
   */
  @RequestMapping(value = "properties", method = RequestMethod.GET)
  public RestResponse<RabbitmqProperties> getProperties() {
    return new RestResponse<>(rabbitmqProperties);
  }

}
