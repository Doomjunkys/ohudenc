/**
 * MailController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.id.web;

import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.itkk.udf.id.IdWorker;
import org.itkk.udf.id.IdWorkerProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 : IdWorkerController
 *
 * @author Administrator
 */
@RestController
public class IdWorkerController implements IIdWorkerController {

    /**
     * idWorker
     */
    @Autowired
    private IdWorker idWorker;

    /**
     * idWorkerProperties
     */
    @Autowired
    private IdWorkerProperties idWorkerProperties;

    @Override
    public RestResponse<String> get() {
        return new RestResponse<>(Long.toString(idWorker.nextId()));
    }

    @Override
    public RestResponse<List<String>> get(@PathVariable Integer count) {
        //判断最大数量
        if (count > idWorkerProperties.getMaxCount()) {
            throw new SystemRuntimeException("The number is too large and the maximum setting is " + idWorkerProperties.getMaxCount());
        }
        //循环生成
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(Long.toString(idWorker.nextId()));
        }
        return new RestResponse<>(ids);
    }
}
