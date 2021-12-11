/**
 * GeneralJob.java
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
import com.egridcloud.udf.scheduler.IGeneralJobResult;
import com.egridcloud.udf.scheduler.client.SchException;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobParam;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobResult;

/**
 * 描述 : GeneralJob
 *
 * @author Administrator
 *
 */
@DisallowConcurrentExecution
public class GeneralJob extends AbstractBaseJob {

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
    GeneralJobParam generalJobParam = new GeneralJobParam();
    generalJobParam.setFireInstanceId(jobExecutionContext.getFireInstanceId());
    generalJobParam.setBeanName(beanName);
    generalJobParam.setJobDataMap(jobDataMap.getWrappedMap());
    //请求
    ResponseEntity<RestResponse<GeneralJobResult>> result = rms.call(serviceCode, generalJobParam,
        null, new ParameterizedTypeReference<RestResponse<GeneralJobResult>>() {
        }, null);
    //判断http状态
    if (result.getStatusCode() != HttpStatus.OK) {
      throw new SchException(result.getStatusCode().toString());
    }
    //记录结果
    saveGeneralJobResult(result.getBody().getResult());
  }

  /**
   * 描述 : 保存(会吃掉注入IGeneralJobResult实现类的异常)
   *
   * @param result result
   */
  private void saveGeneralJobResult(GeneralJobResult result) {
    String[] beanNames = this.getApplicationContext().getBeanNamesForType(IGeneralJobResult.class);
    if (ArrayUtils.isNotEmpty(beanNames)) {
      IGeneralJobResult generalJobResultService =
          this.getApplicationContext().getBean(IGeneralJobResult.class);
      generalJobResultService.save(result);
    }
  }

}
