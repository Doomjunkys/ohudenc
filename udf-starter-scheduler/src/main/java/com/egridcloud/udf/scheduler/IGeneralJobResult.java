/**
 * IGeneralJobResult.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import com.egridcloud.udf.scheduler.client.domain.GeneralJobResult;

/**
 * 描述 : IGeneralJobResult
 *
 * @author Administrator
 *
 */
public interface IGeneralJobResult {

  /**
   * 描述 : save
   *
   * @param generalJobResult generalJobResult
   */
  public void save(GeneralJobResult generalJobResult);

}
