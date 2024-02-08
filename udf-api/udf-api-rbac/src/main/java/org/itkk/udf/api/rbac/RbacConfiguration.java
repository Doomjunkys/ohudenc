package org.itkk.udf.api.rbac;

import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.rbac.auth.CheckApiWebHandlerInterceptor;
import org.itkk.udf.api.rbac.auth.WebHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
@Configuration
public class RbacConfiguration implements WebMvcConfigurer {
    /**
     * webHandlerInterceptor
     */
    @Autowired
    private WebHandlerInterceptor webHandlerInterceptor;

    /**
     * checkApiWebHandlerInterceptor
     */
    @Autowired
    private CheckApiWebHandlerInterceptor checkApiWebHandlerInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webHandlerInterceptor).order(1).addPathPatterns(CommonConstant.URL_ROOT_WEB_PRIVATE + "**");
        registry.addInterceptor(checkApiWebHandlerInterceptor).order(2).addPathPatterns(CommonConstant.URL_ROOT_WEB_PRIVATE + "**");
    }
}
