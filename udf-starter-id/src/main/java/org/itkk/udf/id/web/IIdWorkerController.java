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
import org.itkk.udf.id.domain.Id;
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
     * 获得分布式ID[IdWorker随机]
     *
     * @return ID
     */
    @ApiOperation(value = "ID_1", notes = "获得分布式ID , IdWorker随机 (注意:ID为长整型,在javascript下会丢失精度,请注意数据类型转换)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string")
    })
    @RequestMapping(method = RequestMethod.GET)
    RestResponse<String> get();

    /**
     * 批量获得分布式ID[IdWorker随机]
     *
     * @param count 数量
     * @return ID
     */
    @ApiOperation(value = "ID_2", notes = "批量获得分布式ID , IdWorker随机 (注意:ID为长整型,在javascript下会丢失精度,请注意数据类型转换)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "count", value = "数量", required = true, dataType = "string")
    })
    @RequestMapping(value = "{count}", method = RequestMethod.GET)
    RestResponse<List<String>> get(@PathVariable Integer count);

    /**
     * 解析分布式ID
     *
     * @param id 分布式ID
     * @return ID
     */
    @ApiOperation(value = "ID_3", notes = "解析分布式ID")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "id", value = "分布式ID", required = true, dataType = "long")
    })
    @RequestMapping(value = "reverse/{id}", method = RequestMethod.GET)
    RestResponse<Id> reverse(@PathVariable long id);

    /**
     * 获得分布式ID[指定IdWorker]
     *
     * @param datacenterId 数据中心ID
     * @param workerId     workerId
     * @return ID
     */
    @ApiOperation(value = "ID_4", notes = "获得分布式ID , 指定IdWorker (注意:ID为长整型,在javascript下会丢失精度,请注意数据类型转换)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "datacenterId", value = "数据中心ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "path", name = "workerId", value = "机器ID", required = true, dataType = "int")
    })
    @RequestMapping(value = "worker/{datacenterId}/{workerId}", method = RequestMethod.GET)
    RestResponse<String> workerGet(@PathVariable Integer datacenterId, @PathVariable Integer workerId);

    /**
     * 批量获得分布式ID[指定IdWorker]
     *
     * @param datacenterId 数据中心ID
     * @param workerId     workerId
     * @param count        数量
     * @return ID
     */
    @ApiOperation(value = "ID_5", notes = "批量获得分布式ID , 指定IdWorker (注意:ID为长整型,在javascript下会丢失精度,请注意数据类型转换)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "datacenterId", value = "数据中心ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "path", name = "workerId", value = "机器ID", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "path", name = "count", value = "数量", required = true, dataType = "string")
    })
    @RequestMapping(value = "worker/{datacenterId}/{workerId}/{count}", method = RequestMethod.GET)
    RestResponse<List<String>> workerGet(@PathVariable Integer datacenterId, @PathVariable Integer workerId, @PathVariable Integer count);

}
