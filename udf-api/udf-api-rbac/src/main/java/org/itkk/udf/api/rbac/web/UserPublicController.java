package org.itkk.udf.api.rbac.web;

import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.rbac.dto.LoginDto;
import org.itkk.udf.api.rbac.dto.RegisteredDto;
import org.itkk.udf.api.rbac.service.UserService;
import org.itkk.udf.starter.core.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping(value = CommonConstant.URL_ROOT_WEB_PUBLIC + "rbac/user")
public class UserPublicController {

    /**
     * httpServletResponse
     */
    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * pmPqrrService
     */
    @Autowired
    private UserService userService;

    /**
     * 登陆
     *
     * @param loginDto loginDto
     * @return RestResponse<String>
     */
    @PostMapping("login")
    public RestResponse<String> login(@RequestBody @Valid LoginDto loginDto) {
        final String token = userService.login(loginDto);
        Cookie cookie = new Cookie(CommonConstant.PARAMETER_NAME_TOKEN, token);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return new RestResponse<>(token);
    }

    /**
     * 注册
     *
     * @param registeredDto registeredDto
     * @return RestResponse<String>
     */
    @PostMapping("registered")
    public RestResponse<String> registered(@RequestBody @Valid RegisteredDto registeredDto) {
        final String token = userService.registered(registeredDto);
        Cookie cookie = new Cookie(CommonConstant.PARAMETER_NAME_TOKEN, token);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
        return new RestResponse<>(token);
    }

}
