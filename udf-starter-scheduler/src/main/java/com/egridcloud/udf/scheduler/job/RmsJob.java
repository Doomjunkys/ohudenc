/**
 * RmsJob.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.scheduler.job;

import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.rms.Rms;
import com.egridcloud.udf.scheduler.IRmsJobLog;
import com.egridcloud.udf.scheduler.client.RmsJobStats;
import com.egridcloud.udf.scheduler.client.SchException;
import com.egridcloud.udf.scheduler.client.TriggerType;
import com.egridcloud.udf.scheduler.client.domain.RmsJobParam;
import com.egridcloud.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : RmsJob
 *
 * @author Administrator
 *
 */
public class RmsJob extends AbstractBaseJob {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RmsJob.class);

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    //从trigger中获得jobDataMap
    JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
    //获得必要字段
    String triggerId = jobDataMap.getString("triggerId");
    triggerId = StringUtils.isBlank(triggerId) ? UUID.randomUUID().toString() : triggerId;
    String triggerType = jobDataMap.getString("triggerType");
    triggerType = StringUtils.isBlank(triggerType) ? TriggerType.TRIGGER.value() : triggerType;
    String serviceCode = jobDataMap.getString("serviceCode");
    String beanName = jobDataMap.getString("beanName");
    Boolean async = jobDataMap.getBoolean("async");
    //封装请求参数
    RmsJobParam rmsJobParam = new RmsJobParam();
    rmsJobParam.setId(triggerId);
    rmsJobParam.setTriggerType(triggerType);
    rmsJobParam.setFireInstanceId(jobExecutionContext.getFireInstanceId());
    rmsJobParam.setBeanName(beanName);
    rmsJobParam.setAsync(async);
    rmsJobParam.setJobDataMap(jobDataMap.getWrappedMap());
    try {
      //校验
      if (StringUtils.isBlank(serviceCode)) {
        throw new SchException("serviceCode can not be empty");
      }
      if (StringUtils.isBlank(rmsJobParam.getBeanName())) {
        throw new SchException("beanName can not be empty");
      }
      //获得RMS
      Rms rms = this.getApplicationContext().getBean(Rms.class);
      //请求  
      ResponseEntity<RestResponse<RmsJobResult>> result = rms.call(serviceCode, rmsJobParam, null,
          new ParameterizedTypeReference<RestResponse<RmsJobResult>>() {
          }, null);
      //判断http状态
      if (result.getStatusCode() != HttpStatus.OK) {
        //抛出异常
        throw new SchException("http状态:" + result.getStatusCode().toString());
      }
      //记录
      saveRmsJobResult(result.getBody().getResult());
    } catch (Exception e) {
      //定义返回值
      RmsJobResult result = new RmsJobResult();
      result.setParam(rmsJobParam);
      result.setId(rmsJobParam.getId());
      result.setStats(RmsJobStats.ERROR.value());
      result.setErrorMsg(ExceptionUtils.getStackTrace(e));
      //记录
      saveRmsJobResult(result);
      //抛出异常
      LOGGER.error("RmsJob error:", e);
      throw new JobExecutionException(e);
    }
  }

  /**
   * 描述 : 保存
   *
   * @param result result
   */
  private void saveRmsJobResult(RmsJobResult result) {
    String[] beanNames = this.getApplicationContext().getBeanNamesForType(IRmsJobLog.class);
    if (ArrayUtils.isNotEmpty(beanNames)) {
      IRmsJobLog rmsJobLog = this.getApplicationContext().getBean(IRmsJobLog.class);
      rmsJobLog.save(result);
    }
  }

}
