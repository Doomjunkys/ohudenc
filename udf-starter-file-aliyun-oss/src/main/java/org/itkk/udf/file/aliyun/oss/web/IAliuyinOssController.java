/**
 * IFileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file.aliyun.oss.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.domain.aliyun.oss.OssParam;
import org.itkk.udf.file.aliyun.oss.api.domain.PolicyResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.io.IOException;

/**
 * 描述 : IAliuyinOssController
 *
 * @author Administrator
 */
@Api(value = "阿里云oss服务", consumes = "application/json", produces = "application/json",
        protocols = "http")
@RequestMapping(value = "/service/aliyun/oss")
public interface IAliuyinOssController {

    /**
     * 描述 : 获得阿里云OSS的policy(公网)
     *
     * @param code code
     * @return 结果
     * @throws IOException 异常
     */
    @ApiOperation(value = "FILE_ALIYUN_OSS_POLICY", notes = "获得阿里云OSS的policy(公网)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "path", name = "code", value = "阿里云oss的配置代码", required = true, dataType = "string")
    })
    @RequestMapping(value = "policy/{code}", method = RequestMethod.POST)
    RestResponse<PolicyResult> policy(@PathVariable String code) throws IOException;

    /**
     * 获得阿里云OSS的对象的签名url
     *
     * @param ossParam ossParam
     * @return 结果
     */
    @ApiOperation(value = "FILE_ALIYUN_OSS_PRESIGNED_URL", notes = "获得阿里云OSS的对象的签名url")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string")
    })
    @RequestMapping(value = "presigned/url", method = RequestMethod.POST)
    RestResponse<String> presignedUrl(@RequestBody @Valid OssParam ossParam);

    /**
     * 删除阿里云OSS对象
     *
     * @param ossParam ossParam
     * @return 结果
     */
    @ApiOperation(value = "FILE_ALIYUN_OSS_DELETE", notes = "删除阿里云OSS对象")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string")
    })
    @RequestMapping(value = "delete", method = RequestMethod.PUT)
    RestResponse<String> delete(@RequestBody @Valid OssParam ossParam);

    /**
     * 判断阿里云OSS对象是否存在
     *
     * @param ossParam ossParam
     * @return 结果
     */
    @ApiOperation(value = "FILE_ALIYUN_OSS_CHECK_EXIST", notes = "判断阿里云OSS对象是否存")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "rmsApplicationName", value = "rms应用名称", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsSign", value = "rms认证秘钥", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "rmsServiceCode", value = "rms接口编号", required = true, dataType = "string")
    })
    @RequestMapping(value = "check/exist", method = RequestMethod.POST)
    RestResponse<Boolean> checkExist(@RequestBody @Valid OssParam ossParam);

}
