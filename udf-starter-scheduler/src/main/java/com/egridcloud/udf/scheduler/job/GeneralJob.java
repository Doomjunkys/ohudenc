/**
 * GeneralJob.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.job;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.egridcloud.udf.scheduler.SchException;
import com.egridcloud.udf.scheduler.client.domain.GeneralJobParam;

/**
 * 描述 : GeneralJob
 *
 * @author Administrator
 *
 */
public class GeneralJob extends AbstractBaseJob {

  @Override
  protected void executeInternal(JobExecutionContext jobExecutionContext)
      throws JobExecutionException {
    //初始化开始时间
    Date serverInitStartDate = new Date();
    //从trigger中获得jobDataMap
    JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
    //获得必要字段
    String serviceCode = jobDataMap.getString("serviceCode");
    String beanName = jobDataMap.getString("beanName");
    Boolean async = jobDataMap.getBoolean("async");
    Boolean ignore = jobDataMap.getBoolean("ignore");
    //校验
    if (StringUtils.isBlank(serviceCode)) {
      throw new SchException("serviceCode can not be empty");
    }
    if (StringUtils.isBlank(beanName)) {
      throw new SchException("beanName can not be empty");
    }
    //封装bean
    GeneralJobParam generalJobParam = new GeneralJobParam();
    generalJobParam.setBeanName(beanName);
    generalJobParam.setServerInitStartDate(serverInitStartDate);
    generalJobParam.setServerStartDate(new Date());
    //判断同步还是异步
    if (async) {
      //保存数据
      //判断是否跳过
      if (ignore) {
        //查询是否有正在执行的任务
        //如果有,直接写入任务,状态为跳过
        //跳出
      }
      //执行任务
      //拿到结果后,更新至数据库
    } else {

    }
  }

}
