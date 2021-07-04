/**
 * Config.java
 * Created at 2016-11-19
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 描述 : Config
 *
 * @author Administrator
 *
 */
@Component
@Configuration
public class RmsConfig {

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

}
