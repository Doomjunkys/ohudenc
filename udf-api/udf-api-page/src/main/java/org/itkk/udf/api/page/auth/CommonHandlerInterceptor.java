package org.itkk.udf.api.page.auth;


import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.CommonUtil;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.api.common.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CommonHandlerInterceptor implements HandlerInterceptor {
    /**
     * iUserService
     */
    @Autowired
    private IUserService iUserService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //判空
        if (modelAndView != null) {
            //获得用户信息
            UserDto userDto = iUserService.infoByToken(CommonUtil.getTokenByCookie(request));
            //判断是否登陆
            if (userDto != null) {
                //放入用户信息
                modelAndView.addObject("userDto", userDto);
            }
        }
    }
}
