package org.itkk.udf.api.rbac.web;

import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.CommonUtil;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.api.rbac.dto.ChangePswdDto;
import org.itkk.udf.api.rbac.dto.UserSaveDto;
import org.itkk.udf.api.rbac.service.UserService;
import org.itkk.udf.starter.core.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping(value = CommonConstant.URL_ROOT_WEB_PRIVATE + "rbac/user")
public class UserController {

    /**
     * httpServletRequest
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * pmPqrrService
     */
    @Autowired
    private UserService userService;

    /**
     * 登出
     *
     * @return RestResponse<String>
     */
    @DeleteMapping("logout")
    public RestResponse<String> logout() {
        userService.logout(CommonUtil.getToken(httpServletRequest));
        return RestResponse.success();
    }

    /**
     * 更改密码
     *
     * @param changePswdDto changePswdDto
     * @return RestResponse<String>
     */
    @PutMapping("change/pswd")
    public RestResponse<String> changePswd(@RequestBody @Valid ChangePswdDto changePswdDto) {
        userService.changePswd(changePswdDto, CommonUtil.getUser(httpServletRequest).getUserId());
        return RestResponse.success();
    }

    /**
     * 保存用户信息
     *
     * @param userSaveDto userSaveDto
     * @param userId      userId
     * @return RestResponse<String>
     */
    @PutMapping("save")
    public RestResponse<String> save(@RequestBody @Valid UserSaveDto userSaveDto, String userId) {
        userService.save(userSaveDto, CommonUtil.getToken(httpServletRequest), CommonUtil.getUser(httpServletRequest).getUserId());
        return RestResponse.success();
    }

    /**
     * 获得用户信息
     *
     * @param userId userId
     * @return RestResponse<UserDto>
     */
    @GetMapping("info/{userId}")
    public RestResponse<UserDto> info(@PathVariable String userId) {
        return new RestResponse<>(userService.info(userId));
    }

    /**
     * 获得用户信息
     *
     * @param token token
     * @return RestResponse<UserDto>
     */
    @GetMapping("info/by/token/{token}")
    public RestResponse<UserDto> infoByToken(@PathVariable String token) {
        return new RestResponse<>(userService.infoByToken(token));
    }

}
