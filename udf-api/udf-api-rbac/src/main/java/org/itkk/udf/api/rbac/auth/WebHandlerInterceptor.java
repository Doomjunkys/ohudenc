package org.itkk.udf.api.rbac.auth;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.CommonProperties;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.itkk.udf.starter.core.CoreUtil;
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

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //排除options
        if (!HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            //获得token
            final String token = CoreUtil.getParameter(request, CommonConstant.PARAMETER_NAME_TOKEN);
            //判空
            if (StringUtils.isBlank(token)) {
                throw new AuthException("缺少必要参数token");
            }
        }
        //正常返回
        return true;
    }

}
