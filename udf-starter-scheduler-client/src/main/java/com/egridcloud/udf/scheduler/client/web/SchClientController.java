/**
 * SchClientController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.web;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.scheduler.client.RmsJobStats;
import com.egridcloud.udf.scheduler.client.SchClientHandle;
import com.egridcloud.udf.scheduler.client.domain.RmsJobParam;
import com.egridcloud.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : SchClientController
 *
 * @author Administrator
 *
 */
@RestController
public class SchClientController implements ISchClientController {

  /**
   * 描述 : schClientHandle
   */
  @Autowired
  private SchClientHandle schClientHandle;

  @Override
  public RestResponse<RmsJobResult> execute(@RequestBody RmsJobParam param) {
    //记录来接收时间
    Date receiveTime = new Date();
    //定义返回值
    RmsJobResult result = new RmsJobResult();
    result.setParam(param);
    result.setClientReceiveTime(receiveTime);
    result.setId(param.getId());
    //执行(区分同步跟异步)
    if (param.getAsync()) {
      schClientHandle.asyncHandle(receiveTime, param);
      result.setStats(RmsJobStats.EXECUTING.value());
    } else {
      result.setClientStartExecuteTime(new Date());
      schClientHandle.handle(param);
      result.setClientEndExecuteTime(new Date());
      result.setStats(RmsJobStats.COMPLETE.value());
    }
    //返回
    return new RestResponse<>(result);
  }

}
