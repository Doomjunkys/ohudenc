/**
 * RmsJob.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.job;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.rms.Rms;
import com.egridcloud.udf.scheduler.IRmsJobLog;
import com.egridcloud.udf.scheduler.client.SchException;
import com.egridcloud.udf.scheduler.client.domain.RmsJobParam;
import com.egridcloud.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : RmsJob
 *
 * @author Administrator
 *
 */
@DisallowConcurrentExecution
public class RmsJob extends AbstractBaseJob {

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    //从trigger中获得jobDataMap
    JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
    //获得必要字段
    String serviceCode = jobDataMap.getString("serviceCode");
    String beanName = jobDataMap.getString("beanName");
    //校验
    if (StringUtils.isBlank(serviceCode)) {
      throw new SchException("serviceCode can not be empty");
    }
    if (StringUtils.isBlank(beanName)) {
      throw new SchException("beanName can not be empty");
    }
    //获得RMS
    Rms rms = this.getApplicationContext().getBean(Rms.class);
    //封装请求参数
    RmsJobParam rmsJobParam = new RmsJobParam();
    rmsJobParam.setFireInstanceId(jobExecutionContext.getFireInstanceId());
    rmsJobParam.setBeanName(beanName);
    rmsJobParam.setJobDataMap(jobDataMap.getWrappedMap());
    //请求
    ResponseEntity<RestResponse<RmsJobResult>> result = rms.call(serviceCode, rmsJobParam, null,
        new ParameterizedTypeReference<RestResponse<RmsJobResult>>() {
        }, null);
    //判断http状态
    if (result.getStatusCode() != HttpStatus.OK) {
      throw new SchException(result.getStatusCode().toString());
    }
    //记录结果
    saveRmsJobResult(result.getBody().getResult());
  }

  /**
   * 描述 : 保存(会吃掉注入IGeneralJobResult实现类的异常)
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
