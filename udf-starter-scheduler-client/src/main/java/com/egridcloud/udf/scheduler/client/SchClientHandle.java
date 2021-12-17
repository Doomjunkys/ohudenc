/**
 * SchClientService.java
 * Created at 2017-06-12
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.client;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.rms.Rms;
import com.egridcloud.udf.scheduler.client.domain.RmsJobParam;
import com.egridcloud.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : SchClientService
 *
 * @author Administrator
 *
 */
@Component
public class SchClientHandle implements ApplicationContextAware {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SchClientHandle.class);

  /**
   * 描述 : spring上下文
   */
  private ApplicationContext applicationContext;

  /**
   * 描述 : rms
   */
  @Autowired
  private Rms rms;

  /**
   * 描述 : 异步执行
   *
   * @param receiveTime 接收时间
   * @param param 参数
   */
  @Async
  public void asyncHandle(Date receiveTime, RmsJobParam param) {
    //定义返回值
    RmsJobResult result = new RmsJobResult();
    result.setParam(param);
    result.setClientReceiveTime(receiveTime);
    result.setFireInstanceId(param.getFireInstanceId());
    //执行(并且记录开始和结束时间)
    result.setClientStartExecuteTime(new Date());
    try {
      this.handle(param);
      result.setClientEndExecuteTime(new Date());
      result.setStats(RmsJobStats.COMPLETE.value());
      //回调
      this.callback(result);
    } catch (Exception e) {
      result.setClientEndExecuteTime(new Date());
      result.setStats(RmsJobStats.ERROR.value());
      result.setErrorMsg(ExceptionUtils.getStackTrace(e));
      //回调
      this.callback(result);
      //抛出异常
      LOGGER.error("asyncHandle error:", e);
      throw new SchException(e);
    }

  }

  /**
   * 描述 : 同步执行
   *
   * @param param 参数
   */
  public void handle(RmsJobParam param) {
    //判断bean是否存在
    if (!applicationContext.containsBean(param.getBeanName())) {
      throw new SchException(param.getBeanName() + " not definition");
    }
    //获得bean
    IExecutor bean = applicationContext.getBean(param.getBeanName(), IExecutor.class);
    //执行
    bean.handle(param.getJobDataMap());
  }

  /**
   * 描述 : 回调(重处理)
   *
   * @param result result
   */
  @Retryable(maxAttempts = 3, value = Exception.class)
  private void callback(RmsJobResult result) {
    LOGGER.info("try to callback");
    final String serviceCode = "SCH_CLIENT_CALLBACK_1";
    rms.call(serviceCode, result, null, new ParameterizedTypeReference<RestResponse<String>>() {
    }, null);
  }

  /**
   * 描述 : 重试失败后执行的方法
   *
   * @param e e
   */
  @Recover
  public void recover(Exception e) {
    LOGGER.error("try to callback failed:", e);
  }

  @Override
  public void setApplicationContext(ApplicationContext arg0) throws BeansException {
    applicationContext = arg0;
  }

}
