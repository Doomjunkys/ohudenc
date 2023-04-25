/**
 * IFileController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.aliyun.dysms.web;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.itkk.udf.core.RestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 描述 : IAliuyunDysmsController
 *
 * @author Administrator
 */
@Api(value = "阿里云sms服务", consumes = "application/json", produces = "application/json",
        protocols = "http")
@RequestMapping(value = "/service/aliyun/dysms")
public interface IAliuyunDysmsController {

    /**
     * 发送短信验证码
     *
     * @return Boolean
     * @throws JsonProcessingException JsonProcessingException
     * @throws ClientException         ClientException
     */
    @ApiOperation(value = "ALIYUN_DYSMS_SEND_VERIFICATION_CODE", notes = "发送短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "phoneNumber", value = "手机号码", required = true, dataType = "string")
    })
    @RequestMapping(value = "send/verification/code", method = RequestMethod.POST)
    RestResponse<Boolean> sendVerificationCode() throws JsonProcessingException, ClientException;

    /**
     * 描述 : 验证短信验证码
     *
     * @return RestResponse<Boolean>
     */
    @ApiOperation(value = "ALIYUN_DYSMS_CHECK_VERIFICATION_CODE", notes = "验证短信验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header", name = "phoneNumber", value = "手机号码", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "header", name = "verificationCode", value = "短信验证码", required = true, dataType = "string")
    })
    @RequestMapping(value = "check/verification/code", method = RequestMethod.POST)
    RestResponse<Boolean> checkVerificationCode();
}
