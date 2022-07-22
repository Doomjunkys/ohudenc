/**
 * MailController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.id.web;

import org.itkk.udf.core.RestResponse;
import org.itkk.udf.id.domain.Id;
import org.itkk.udf.id.service.IdWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 描述 : IdWorkerController
 *
 * @author Administrator
 */
@RestController
public class IdWorkerController implements IIdWorkerController {

    /**
     * idWorkerService
     */
    @Autowired
    private IdWorkerService idWorkerService;

    @Override
    public RestResponse<String> get() {
        return new RestResponse<>(idWorkerService.get());
    }

    @Override
    public RestResponse<List<String>> get(@PathVariable Integer count) {
        return new RestResponse<>(idWorkerService.get(count));
    }

    @Override
    public RestResponse<Id> reverse(@PathVariable long id) {
        return new RestResponse<>(idWorkerService.reverse(id));
    }

    @Override
    public RestResponse<String> workerGet(@PathVariable Integer datacenterId, @PathVariable Integer workerId) {
        return new RestResponse<>(idWorkerService.workerGet(datacenterId, workerId));
    }

    @Override
    public RestResponse<List<String>> workerGet(@PathVariable Integer datacenterId, @PathVariable Integer workerId, @PathVariable Integer count) {
        return new RestResponse<>(idWorkerService.workerGet(datacenterId, workerId, count));
    }
}
