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
   * @param generalJobResult generalJobResult
   */
  public void save(RmsJobResult generalJobResult);

}
