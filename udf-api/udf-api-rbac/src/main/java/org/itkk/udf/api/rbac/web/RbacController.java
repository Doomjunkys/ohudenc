package org.itkk.udf.api.rbac.web;


import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.CommonUtil;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.api.rbac.dto.MenuDto;
import org.itkk.udf.api.rbac.service.RbacService;
import org.itkk.udf.starter.core.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = CommonConstant.URL_ROOT_WEB_PRIVATE + "rbac")
public class RbacController {

    /**
     * httpServletRequest
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * rbacService
     */
    @Autowired
    private RbacService rbacService;

    /**
     * 获得用户菜单
     *
     * @return RestResponse<List < MenuDto>>
     */
    @GetMapping("menu")
    public RestResponse<List<MenuDto>> menu() {
        String token = CommonUtil.getToken(httpServletRequest);
        UserDto userDto = CommonUtil.getUser(httpServletRequest);
        return new RestResponse<>(rbacService.loadMenu(token, userDto.getUserId()));
    }

    /**
     * 获得所有菜单
     *
     * @return RestResponse<List < MenuDto>>
     */
    @GetMapping("menu/all")
    public RestResponse<List<MenuDto>> loadAllMenu() {
        return new RestResponse<>(rbacService.loadAllMenu());
    }

}
