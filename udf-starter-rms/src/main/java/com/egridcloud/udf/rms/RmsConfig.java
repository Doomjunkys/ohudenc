/**
 * Config.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 描述 : Config
 *
 * @author Administrator
 *
 */
@Configuration
@ConfigurationProperties(prefix = "com.egridcloud.rms.config")
public class RmsConfig {

  /**
   * 描述 : rms扫描路径
   */
  private String rmsPathPatterns;

  /**
   * 描述 : restTemplate
   *
   * @return restTemplate
   */
  @Bean
  @LoadBalanced
  RestTemplate restTemplate() {
    return new RestTemplate();
  }

  /**
   * 描述 : rmsAuthConfigurer
   *
   * @return WebMvcConfigurer
   */
  @Bean
  public WebMvcConfigurer rmsAuthConfigurer() { //NOSONAR
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addInterceptors(InterceptorRegistry registry) {
        String[] rmsPathPatternsArray = rmsPathPatterns.split(",");
        registry.addInterceptor(rmsAuthHandlerInterceptor()).addPathPatterns(rmsPathPatternsArray);
        super.addInterceptors(registry);
      }
    };
  }

  /**
   * 描述 : RmsAuthHandlerInterceptor
   *
   * @return rmsAuthHandlerInterceptor
   */
  @Bean
  public RmsAuthHandlerInterceptor rmsAuthHandlerInterceptor() {
    return new RmsAuthHandlerInterceptor();
  }

  /**
   * 描述 : 获取rmsPathPatterns
   *
   * @return the rmsPathPatterns
   */
  public String getRmsPathPatterns() {
    return rmsPathPatterns;
  }

  /**
   * 描述 : 设置rmsPathPatterns
   *
   * @param rmsPathPatterns the rmsPathPatterns to set
   */
  public void setRmsPathPatterns(String rmsPathPatterns) {
    this.rmsPathPatterns = rmsPathPatterns;
  }

}
