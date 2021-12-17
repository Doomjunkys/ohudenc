/**
 * IRmsJobLog.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import com.egridcloud.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : IRmsJobLog
 *
 * @author Administrator
 *
 */
public interface IRmsJobLog {

  /**
   * 描述 : save
   *
   * @param result result
   */
  public void save(RmsJobResult result);

  /**
   * 描述 : delete
   *
   * @param fireInstanceId fireInstanceId
   */
  public void delete(String fireInstanceId);

}
