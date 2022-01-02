/**
 * RabbitmqController.java
 * Created at 2017-05-25
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.amqp.rabbitmq.web;

import org.itkk.udf.amqp.rabbitmq.RabbitmqProperties;
import org.itkk.udf.core.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 描述 : RabbitmqController
 *
 * @author Administrator
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
