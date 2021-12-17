/**
 * IExecutor.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client;

import java.util.Map;

/**
 * 描述 : IExecutor
 *
 * @author Administrator
 *
 */
public interface IExecutor {

  /**
   * 描述 : 执行方法
   *
   * @param jobDataMap jobDataMap
   */
  public void handle(Map<String, Object> jobDataMap);

}
