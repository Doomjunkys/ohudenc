/**
 * RmsJob.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.job;

import java.util.UUID;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.rms.Rms;
import org.itkk.udf.scheduler.IRmsJobLog;
import org.itkk.udf.scheduler.client.RmsJobStats;
import org.itkk.udf.scheduler.client.SchException;
import org.itkk.udf.scheduler.client.TriggerDataMapKey;
import org.itkk.udf.scheduler.client.TriggerType;
import org.itkk.udf.scheduler.client.domain.RmsJobParam;
import org.itkk.udf.scheduler.client.domain.RmsJobResult;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 描述 : RmsJob
 *
 * @author Administrator
 */
public class RmsJob extends AbstractBaseJob {

    /**
     * 描述 : 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RmsJob.class);

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException { //NOSONAR
        //从trigger中获得jobDataMap
        JobDataMap jobDataMap = jobExecutionContext.getTrigger().getJobDataMap();
        //获得必要字段
        String triggerId = jobDataMap.getString(TriggerDataMapKey.TRIGGER_ID.value());
        triggerId = StringUtils.isBlank(triggerId) ? UUID.randomUUID().toString() : triggerId;
        String parentTriggerId = jobDataMap.getString(TriggerDataMapKey.PARENT_TRIGGER_ID.value());
        String triggerType = jobDataMap.getString(TriggerDataMapKey.TRIGGER_TYPE.value());
        triggerType = StringUtils.isBlank(triggerType) ? TriggerType.TRIGGER.value() : triggerType;
        String serviceCode = jobDataMap.getString(TriggerDataMapKey.SERVICE_CODE.value());
        String beanName = jobDataMap.getString(TriggerDataMapKey.BEAN_NAME.value());
        Boolean async = jobDataMap.getBoolean(TriggerDataMapKey.ASYNC.value());
        //封装请求参数
        RmsJobParam rmsJobParam = new RmsJobParam();
        rmsJobParam.setId(triggerId);
        rmsJobParam.setParentId(parentTriggerId);
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
            ResponseEntity<RestResponse<RmsJobResult>> result = rms.call(serviceCode, rmsJobParam, null, new ParameterizedTypeReference<RestResponse<RmsJobResult>>() {
            }, null);
            //判断http状态
            if (result.getStatusCode() != HttpStatus.OK) {
                //抛出异常
                throw new SchException("http状态:" + result.getStatusCode().toString());
            }
            //记录
            saveRmsJobLog(rmsJobParam, result.getBody().getResult());
        } catch (Exception e) {
            //定义返回值
            RmsJobResult result = new RmsJobResult();
            result.setId(rmsJobParam.getId());
            result.setStats(RmsJobStats.ERROR.value());
            result.setErrorMsg(ExceptionUtils.getStackTrace(e));
            //记录
            saveRmsJobLog(rmsJobParam, result);
            //抛出异常
            LOGGER.error("RmsJob error:", e);
            throw new JobExecutionException(e);
        }
    }

    /**
     * 保存
     *
     * @param param param
     * @param result result
     */
    private void saveRmsJobLog(RmsJobParam param, RmsJobResult result) {
        String[] beanNames = this.getApplicationContext().getBeanNamesForType(IRmsJobLog.class);
        if (ArrayUtils.isNotEmpty(beanNames)) {
            IRmsJobLog rmsJobLog = this.getApplicationContext().getBean(IRmsJobLog.class);
            rmsJobLog.save(param, result);
        }
    }

}
