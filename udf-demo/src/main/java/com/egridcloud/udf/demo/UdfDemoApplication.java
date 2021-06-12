/**
 * BaseApplication.java
 * Created at 2016-10-02
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

import com.egridcloud.udf.core.BaseApplication;

/**
 * 描述 : 系统入口
 *
 * @author wangkang
 *
 */
@SpringCloudApplication
public class UdfDemoApplication extends BaseApplication {

  /**
   * 描述 : spring boot的入口，在整个项目中，包括其子项目在内， 只能有一个main方法，否则spring boot启动不起来
   *
   * @param args 参数
   */
  public static void main(String[] args) {
    SpringApplication.run(UdfDemoApplication.class, args);
  }

}
