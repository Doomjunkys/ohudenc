package org.itkk.udf.auth.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.itkk.udf.auth.Constant;
import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;
import org.itkk.udf.core.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * IUserAuthenticationController
 */
@Api(value = "用户鉴权服务", consumes = "application/json", produces = "application/json", protocols = "http")
@RequestMapping(value = Constant.USER_AUTHENTICATION_URL)
public interface IUserAuthenticationController { //NOSONAR

    /**
     * 用户鉴权
     *
     * @param model 用户鉴权输入模型
     * @return 用户鉴权响应模型
     */
    @ApiOperation(value = "AUTH_USER_1", notes = "用户鉴权")
    @PostMapping
    RestResponse<UserAuthenticationResult> authentication(@RequestBody UserAuthenticationModel model);

}
