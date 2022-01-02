/**
 * JobController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.itkk.udf.core.RestResponse;
import org.itkk.udf.scheduler.IRmsJobLog;
import org.itkk.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : JobController
 *
 * @author Administrator
 */
@RestController
public class SchClientCallbackController implements ISchClientCallbackController {

    /**
     * 描述 : rmsJobLog
     */
    @Autowired(required = false)
    private IRmsJobLog rmsJobLog;

    @Override
    public RestResponse<String> callback(@RequestBody RmsJobResult result) {
        if (rmsJobLog != null) {
            rmsJobLog.save(result);
        }
        return new RestResponse<>();
    }

}
