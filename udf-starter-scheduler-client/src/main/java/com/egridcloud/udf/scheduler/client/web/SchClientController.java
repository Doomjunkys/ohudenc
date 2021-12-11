/**
 * SchClientController.java
 * Created at 2017-05-26
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client.web;

import java.util.Date;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.scheduler.client.IExecute;
import com.egridcloud.udf.scheduler.client.SchException;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobParam;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobResult;

/**
 * 描述 : SchClientController
 *
 * @author Administrator
 *
 */
@RestController
public class SchClientController implements ISchClientController, ApplicationContextAware {

  /**
   * 描述 : spring上下文
   */
  private ApplicationContext applicationContext;

  @Override
  public RestResponse<GeneralJobResult> execute(@RequestBody GeneralJobParam param) {
    //客户端初始化时间
    Date clientInitTime = new Date();
    //定义返回值
    GeneralJobResult result = new GeneralJobResult();
    result.setFireInstanceId(param.getFireInstanceId());
    result.setParam(param);
    //判断bean是否存在
    if (!applicationContext.containsBean(param.getBeanName())) {
      throw new SchException(param.getBeanName() + " not definition");
    }
    //获得bean
    IExecute bean = applicationContext.getBean(param.getBeanName(), IExecute.class);
    //客户端开始时间
    Date clientStartExecuteTime = new Date();
    //执行
    bean.execute(param.getJobDataMap());
    //客户端结束时间
    Date clientEndExecuteTime = new Date();
    //设置返回值
    result.setClientInitTime(clientInitTime);
    result.setClientStartExecuteTime(clientStartExecuteTime);
    result.setClientEndExecuteTime(clientEndExecuteTime);
    //返回
    return new RestResponse<>(result);
  }

  @Override
  public void setApplicationContext(ApplicationContext arg0) throws BeansException {
    applicationContext = arg0;
  }

}
