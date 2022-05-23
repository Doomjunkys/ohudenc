/**
 * IIdWorkerController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.id.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.itkk.udf.core.RestResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 描述 : IIdWorkerController
 *
 * @author Administrator
 */
@Api(value = "分布式ID服务", consumes = "application/json", produces = "application/json", protocols = "http")
@RequestMapping(value = "/service/id")
public interface IIdWorkerController { //NOSONAR

    /**
     * 最大个数
     */
    int MAX_COUNT = 1000;

    /**
     * 获得分布式ID
     *
     * @return ID
     */
    @ApiOperation(value = "ID_1", notes = "获得分布式ID(注意:ID为长整型,在javascript下会丢失精度,请注意数据类型转换)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string")
    })
    @RequestMapping(method = RequestMethod.GET)
    RestResponse<String> get();

    /**
     * 批量获得分布式ID
     *
     * @param count 数量
     * @return ID
     */
    @ApiOperation(value = "ID_2", notes = "批量获得分布式ID(注意:ID为长整型,在javascript下会丢失精度,请注意数据类型转换)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "count", value = "数量", required = true, dataType = "string")
    })
    @RequestMapping(value = "{count}", method = RequestMethod.GET)
    RestResponse<List<String>> get(@PathVariable Integer count);

}
