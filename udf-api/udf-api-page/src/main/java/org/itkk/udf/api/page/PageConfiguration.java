package org.itkk.udf.api.page;

import org.itkk.udf.api.page.auth.CommonHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
@Configuration
public class PageConfiguration implements WebMvcConfigurer {
    /**
     * commonHandlerInterceptor
     */
    @Autowired
    private CommonHandlerInterceptor commonHandlerInterceptor;

    /**
     * 添加拦截器
     *
     * @param registry registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(commonHandlerInterceptor).order(1).addPathPatterns("/**");
    }
}
