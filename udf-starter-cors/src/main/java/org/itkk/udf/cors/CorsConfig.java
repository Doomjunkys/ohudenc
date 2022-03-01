/**
 * CorsConfig.java
 * Created at 2017-04-25
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.cors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * 描述 : CorsConfig ( another way @see EndpointCorsProperties.java )
 *
 * @author Administrator
 */
@Configuration
@ConfigurationProperties(prefix = "org.itkk.cors")
@Validated
public class CorsConfig {

    /**
     * 描述 : 跨域信息
     */
    @NotNull
    private Map<String, CorsRegistrationConfig> config;

    /**
     * 描述 : corsConfigurer
     *
     * @return WebMvcConfigurer
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() { //NOSONAR
        return new WebMvcConfigurerAdapter() { //NOSONAR
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //扫描地址
                if (!CollectionUtils.isEmpty(config)) {
                    config.forEach((key, item) -> {
                        CorsRegistration cr = registry.addMapping(item.getMapping());
                        if (item.getAllowCredentials() != null) {
                            cr.allowCredentials(item.getAllowCredentials());
                        }
                        if (StringUtils.isNotBlank(item.getAllowedOrigins())) {
                            String[] allowedOriginArray = item.getAllowedOrigins().split(",");
                            cr.allowedOrigins(allowedOriginArray);
                        }
                        if (StringUtils.isNotBlank(item.getAllowedMethods())) {
                            String[] allowedMethodArray = item.getAllowedMethods().split(",");
                            cr.allowedMethods(allowedMethodArray);
                        }
                        if (StringUtils.isNotBlank(item.getAllowedHeaders())) {
                            String[] allowedHeaderArray = item.getAllowedHeaders().split(",");
                            cr.allowedHeaders(allowedHeaderArray);
                        }
                    });
                }
            }
        };
    }

    /**
     * 描述 : 获取config
     *
     * @return the config
     */
    public Map<String, CorsRegistrationConfig> getConfig() {
        return config;
    }

    /**
     * 描述 : 设置config
     *
     * @param config the config to set
     */
    public void setConfig(Map<String, CorsRegistrationConfig> config) {
        this.config = config;
    }

    /**
     * 描述 : 跨域信息
     *
     * @author Administrator
     */
    public static class CorsRegistrationConfig {
        /**
         * 描述 : 扫描地址
         */
        private String mapping = "/**";

        /**
         * 描述 : 允许cookie
         */
        private Boolean allowCredentials = true;

        /**
         * 描述 : 允许的域
         */
        private String allowedOrigins = "*";

        /**
         * 描述 : 允许的方法
         */
        private String allowedMethods = "POST,GET,DELETE,PUT";

        /**
         * 描述 : 允许的头信息
         */
        private String allowedHeaders = "*";

        /**
         * 描述 : 获取mapping
         *
         * @return the mapping
         */
        public String getMapping() {
            return mapping;
        }

        /**
         * 描述 : 设置mapping
         *
         * @param mapping the mapping to set
         */
        public void setMapping(String mapping) {
            this.mapping = mapping;
        }

        /**
         * 描述 : 获取allowCredentials
         *
         * @return the allowCredentials
         */
        public Boolean getAllowCredentials() {
            return allowCredentials;
        }

        /**
         * 描述 : 设置allowCredentials
         *
         * @param allowCredentials the allowCredentials to set
         */
        public void setAllowCredentials(Boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
        }

        /**
         * 描述 : 获取allowedOrigins
         *
         * @return the allowedOrigins
         */
        public String getAllowedOrigins() {
            return allowedOrigins;
        }

        /**
         * 描述 : 设置allowedOrigins
         *
         * @param allowedOrigins the allowedOrigins to set
         */
        public void setAllowedOrigins(String allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
        }

        /**
         * 描述 : 获取allowedMethods
         *
         * @return the allowedMethods
         */
        public String getAllowedMethods() {
            return allowedMethods;
        }

        /**
         * 描述 : 设置allowedMethods
         *
         * @param allowedMethods the allowedMethods to set
         */
        public void setAllowedMethods(String allowedMethods) {
            this.allowedMethods = allowedMethods;
        }

        /**
         * 描述 : 获取allowedHeaders
         *
         * @return the allowedHeaders
         */
        public String getAllowedHeaders() {
            return allowedHeaders;
        }

        /**
         * 描述 : 设置allowedHeaders
         *
         * @param allowedHeaders the allowedHeaders to set
         */
        public void setAllowedHeaders(String allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
        }

    }

}
