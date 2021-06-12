/**
 * BaseApplication.java
 * Created at 2016-10-02
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;

import com.egridcloud.udf.core.xss.XssStringJsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * 描述 : 系统入口
 *
 * @author wangkang
 *
 */
@ServletComponentScan
@ComponentScan(basePackages = { "com.egridcloud" })
@EnableAsync
@RefreshScope
public class BaseApplication {

  /**
   * 描述 : xssObjectMapper
   *
   * @param builder builder
   * @return xssObjectMapper
   */
  @Bean
  public ObjectMapper xssObjectMapper(Jackson2ObjectMapperBuilder builder) {
    //解析器
    ObjectMapper objectMapper = builder.createXmlMapper(false).build();
    //注册xss解析器
    SimpleModule xssModule = new SimpleModule("XssStringJsonSerializer");
    xssModule.addSerializer(new XssStringJsonSerializer());
    objectMapper.registerModule(xssModule);
    //返回
    return objectMapper;
  }

}
