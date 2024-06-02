package org.itkk.udf.api.rbac.auth;


import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.CommonProperties;
import org.itkk.udf.api.common.CommonUtil;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.api.common.service.IUserService;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class WebHandlerInterceptor implements HandlerInterceptor {
    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    /**
     * commonPropertiesl
     */
    @Autowired
    private CommonProperties commonProperties;

    /**
     * iUserService
     */
    @Autowired
    private IUserService iUserService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //排除options
        if (!HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            //获得token
            final String token = CommonUtil.getToken(request);
            //获得tokenInfo
            UserDto userDto = iUserService.infoByToken(token);
            //判空
            if (userDto == null) {
                throw new AuthException("token不存在");
            }
            //放入请求作用域
            request.setAttribute(CommonConstant.PARAMETER_NAME_TOKEN_INFO, userDto);
            request.setAttribute(CommonConstant.PARAMETER_NAME_TOKEN, token);
            //刷新token ttl
            dbCacheService.setTtl(token, CommonConstant.TOKEN_CACHE_TTL);
        }
        //正常返回
        return true;
    }

}
