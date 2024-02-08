package org.itkk.udf.api.rbac.auth;


import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.CommonUtil;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.api.rbac.service.RbacService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CheckApiWebHandlerInterceptor implements HandlerInterceptor {

    /**
     * rbacService
     */
    @Autowired
    private RbacService rbacService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //排除options
        if (!HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            //获得数据
            String token = CommonUtil.getToken(request);
            UserDto userDto = CommonUtil.getUser(request);
            String uri = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
            String method = request.getMethod();
            //检查权限
            rbacService.checkApi(token, userDto.getUserId(), uri, method);
        }
        //正常返回
        return true;
    }
}
