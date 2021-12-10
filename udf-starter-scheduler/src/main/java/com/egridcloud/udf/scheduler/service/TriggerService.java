/**
 * TriggerService.java
 * Created at 2017-06-02
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.egridcloud.udf.core.utils.DateUtil;
import com.egridcloud.udf.scheduler.SchedulerProperties;
import com.egridcloud.udf.scheduler.client.SchException;
import com.egridcloud.udf.scheduler.meta.CronTriggerMeta;
import com.egridcloud.udf.scheduler.meta.JobDetailMeta;
import com.egridcloud.udf.scheduler.meta.SimpleTriggerMeta;
import com.egridcloud.udf.scheduler.meta.TriggerMeta;

/**
 * 描述 : TriggerService
 *
 * @author Administrator
 *
 */
@Service
public class TriggerService {

  /**
   * 描述 : SchedulerFactoryBean
   */
  @Autowired
  @Qualifier("clusterQuartzScheduler")
  private SchedulerFactoryBean s;

  /**
   * 描述 : jobService
   */
  @Autowired
  private JobService jobService;

  /**
   * 描述 : schedulerProperties
   */
  @Autowired
  private SchedulerProperties schedulerProperties;

  /**
   * 描述 : 触发器组
   *
   * @return 触发器组
   */
  public Map<String, String> group() {
    return schedulerProperties.getTriggerGroup();
  }

  /**
   * 描述 : simple的MisfireInstruction
   *
   * @return simple的MisfireInstruction
   */
  public Map<Integer, String> simpleMisfireInstruction() {
    //定义
    Map<Integer, String> result = new HashMap<>();
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW, "Fire Now");
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT,
        "Reschedule Next With Existing Count");
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT,
        "Reschedule Next With Remaining Count");
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT,
        "Now With Existing Repeat Count");
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT,
        "Reschedule Now With Remaining Repeat Count");
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY, "Ignore Misfire Policy");
    result.put(SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY, "Smart Policy");
    //返回
    return result;
  }

  /**
   * 描述 : cron的MisfireInstruction
   *
   * @return cron的MisfireInstruction
   */
  public Map<Integer, String> cronMisfireInstruction() {
    //定义
    Map<Integer, String> result = new HashMap<>();
    result.put(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING, "Do Nothing");
    result.put(CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW, "Fire Once Now");
    result.put(CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY, "Ignore Misfire Policy");
    result.put(CronTrigger.MISFIRE_INSTRUCTION_SMART_POLICY, "Smart Policy");
    //返回
    return result;
  }

  /**
   * 描述 : 日历列表
   *
   * @return 日历列表
   * @throws SchedulerException SchedulerException
   */
  public List<String> calendar() throws SchedulerException {
    return this.s.getScheduler().getCalendarNames();
  }

  /**
   * 描述 : 时区列表
   *
   * @return 时区列表
   */
  public Map<String, String> timeZoneId() {
    Map<String, String> result = new HashMap<>();
    List<String> jgl = DateUtil.fecthAllTimeZoneIds();
    if (CollectionUtils.isNotEmpty(jgl)) {
      for (String item : jgl) {
        result.put(item, "(UTC+" + DateUtil.getUtcTimeZoneRawOffset(item) + ")" + item);
      }
    }
    return result;
  }

  /**
   * 描述 : 保存cron触发器
   * 
   * @param cronTriggerCode cron触发器代码
   * @param cover 是否覆盖
   * @throws SchedulerException SchedulerException
   */
  public void saveCronTrigger(String cronTriggerCode, boolean cover) throws SchedulerException {
    //获得cron触发器元数据
    CronTriggerMeta triggerMeta = getCronTriggerMeta(cronTriggerCode);
    // 获得计划任务管理器
    Scheduler sch = this.s.getScheduler();
    //获得触发器键
    TriggerKey triggerKey = new TriggerKey(triggerMeta.getName(), triggerMeta.getGroup());
    //判断触发器是否存在
    if (sch.checkExists(triggerKey) && !cover) {
      throw new SchException("trigger key : " + triggerMeta.getName() + " / group : " //NOSONAR
          + triggerMeta.getGroup() + " already exist!");
    }
    //获得作业元数据
    JobDetailMeta jobDetailMeta = jobService.getJobDetailMeta(triggerMeta.getJobCode());
    // 获得jobkey
    JobKey jobKey = jobService.getJobKey(jobDetailMeta);
    // 获得jobDataMap
    JobDataMap jobDataMap = null;
    if (MapUtils.isNotEmpty(triggerMeta.getDataMap())) {
      jobDataMap = new JobDataMap(triggerMeta.getDataMap());
    }
    // 获得triggerBuilder
    TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
    // 设置triggerBuilder
    triggerBuilder.withIdentity(triggerKey); // 设置触发器对象
    triggerBuilder.usingJobData(jobDataMap); // 设置jobDataMap
    triggerBuilder.withPriority(triggerMeta.getPriority()); // 设置优先级
    if (triggerMeta.getStartTime() == null) { // 设置开始时间
      triggerBuilder.startNow();
    } else {
      triggerBuilder.startAt(triggerMeta.getStartTime());
    }
    triggerBuilder.endAt(triggerMeta.getEndTime()); // 结束时间,可以为空
    triggerBuilder.forJob(jobKey); // 针对的作业
    triggerBuilder.withDescription(triggerMeta.getDescription()); // 设置描述
    if (!StringUtils.isEmpty(triggerMeta.getCalendar())) { // 设置日历
      triggerBuilder.modifiedByCalendar(triggerMeta.getCalendar());
    }
    // cron表达式
    CronScheduleBuilder cronTriggerBuilder =
        CronScheduleBuilder.cronSchedule(triggerMeta.getCron());
    // 时区
    cronTriggerBuilder.inTimeZone(TimeZone.getTimeZone(triggerMeta.getTimeZoneId()));
    // 设置触发机制 默认 SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY
    if (CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING == triggerMeta.getMisfireInstruction()) {
      cronTriggerBuilder.withMisfireHandlingInstructionDoNothing();
    } else if (CronTrigger.MISFIRE_INSTRUCTION_FIRE_ONCE_NOW == triggerMeta
        .getMisfireInstruction()) {
      cronTriggerBuilder.withMisfireHandlingInstructionFireAndProceed();
    } else if (CronTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY == triggerMeta
        .getMisfireInstruction()) {
      cronTriggerBuilder.withMisfireHandlingInstructionIgnoreMisfires();
    }
    // 将cronTrigger设置到triggerBuilder中
    triggerBuilder.withSchedule(cronTriggerBuilder);
    // 构造Trigger
    Trigger simpleTrigger = triggerBuilder.build();
    // 判断trigger是否存在
    if (sch.checkExists(triggerKey)) {
      // 获得当前触发器
      Trigger t = sch.getTrigger(triggerKey);
      // 判断触发器关联
      if (jobKey.getName().equals(t.getJobKey().getName())
          && jobKey.getGroup().equals(t.getJobKey().getGroup())) {
        // 更新触发器
        sch.rescheduleJob(triggerKey, simpleTrigger);
      } else {
        throw new SchException("rescheduleJob fail");
      }
    } else {
      // 创建触发器
      sch.scheduleJob(simpleTrigger);
    }
  }

  /**
   * 描述 : 保存simple触发器
   * 
   * @param simpleTriggerCode simple触发器代码
   * @param cover 是否覆盖
   * @throws SchedulerException SchedulerException
   */
  public void saveSimpleTirgger(String simpleTriggerCode, boolean cover) throws SchedulerException { //NOSONAR
    //获得simple触发器元数据
    SimpleTriggerMeta triggerMeta = getSimpleTriggerMeta(simpleTriggerCode);
    // 获得计划任务管理器
    Scheduler sch = this.s.getScheduler();
    //获得触发器键
    TriggerKey triggerKey = new TriggerKey(triggerMeta.getName(), triggerMeta.getGroup());
    //判断触发器是否存在
    if (sch.checkExists(triggerKey) && !cover) {
      throw new SchException("trigger key : " + triggerMeta.getName() + " / group : " //NOSONAR
          + triggerMeta.getGroup() + " already exist!");
    }
    //获得作业元数据
    JobDetailMeta jobDetailMeta = jobService.getJobDetailMeta(triggerMeta.getJobCode());
    // 获得jobkey
    JobKey jobKey = jobService.getJobKey(jobDetailMeta);
    // 获得jobDataMap
    JobDataMap jobDataMap = null;
    if (MapUtils.isNotEmpty(triggerMeta.getDataMap())) {
      jobDataMap = new JobDataMap(triggerMeta.getDataMap());
    }
    // 获得triggerBuilder
    TriggerBuilder<Trigger> triggerBuilder = TriggerBuilder.newTrigger();
    // 设置triggerBuilder
    triggerBuilder.withIdentity(triggerKey); // 设置触发器对象
    triggerBuilder.usingJobData(jobDataMap); // 设置jobDataMap
    triggerBuilder.withPriority(triggerMeta.getPriority()); // 设置优先级
    if (triggerMeta.getStartTime() == null) { // 设置开始时间
      triggerBuilder.startNow();
    } else {
      triggerBuilder.startAt(triggerMeta.getStartTime());
    }
    triggerBuilder.endAt(triggerMeta.getEndTime()); // 结束时间,可以为空
    triggerBuilder.forJob(jobKey); // 针对的作业
    triggerBuilder.withDescription(triggerMeta.getDescription()); // 设置描述
    if (!StringUtils.isEmpty(triggerMeta.getCalendar())) { // 设置日历
      triggerBuilder.modifiedByCalendar(triggerMeta.getCalendar());
    }
    // 获得SimpleTrigger
    SimpleScheduleBuilder simpleTriggerBuilder = SimpleScheduleBuilder.simpleSchedule();
    if (triggerMeta.getRepeatCount() != null) { // 重复次数
      simpleTriggerBuilder.withRepeatCount(triggerMeta.getRepeatCount());
    } else {
      simpleTriggerBuilder.repeatForever();
    }
    // 重复时间
    simpleTriggerBuilder.withIntervalInMilliseconds(triggerMeta.getIntervalInMilliseconds());
    // 设置触发机制 默认 SimpleTrigger.MISFIRE_INSTRUCTION_SMART_POLICY
    if (SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW == triggerMeta.getMisfireInstruction()) {
      simpleTriggerBuilder.withMisfireHandlingInstructionFireNow();
    } else if (SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_EXISTING_COUNT == triggerMeta
        .getMisfireInstruction()) {
      simpleTriggerBuilder.withMisfireHandlingInstructionNextWithExistingCount();
    } else if (SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT == triggerMeta
        .getMisfireInstruction()) {
      simpleTriggerBuilder.withMisfireHandlingInstructionNextWithRemainingCount();
    } else if (SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_EXISTING_REPEAT_COUNT == triggerMeta
        .getMisfireInstruction()) {
      simpleTriggerBuilder.withMisfireHandlingInstructionNowWithExistingCount();
    } else if (SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NOW_WITH_REMAINING_REPEAT_COUNT == triggerMeta
        .getMisfireInstruction()) {
      simpleTriggerBuilder.withMisfireHandlingInstructionNowWithRemainingCount();
    } else if (SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY == triggerMeta
        .getMisfireInstruction()) {
      simpleTriggerBuilder.withMisfireHandlingInstructionIgnoreMisfires();
    }
    // 将SimpleTrigger设置到triggerBuilder中
    triggerBuilder.withSchedule(simpleTriggerBuilder);
    // 构造Trigger
    Trigger simpleTrigger = triggerBuilder.build();
    // 判断trigger是否存在
    if (sch.checkExists(triggerKey)) {
      // 获得当前触发器
      Trigger t = sch.getTrigger(triggerKey);
      // 判断触发器关联
      if (jobKey.getName().equals(t.getJobKey().getName())
          && jobKey.getGroup().equals(t.getJobKey().getGroup())) {
        // 更新触发器
        sch.rescheduleJob(triggerKey, simpleTrigger);
      } else {
        throw new SchException("rescheduleJob fail");
      }
    } else {
      // 创建触发器
      sch.scheduleJob(simpleTrigger);
    }
  }

  /**
   * 描述 : 移除
   *
   * @param triggerCode triggerCode
   * @throws SchedulerException SchedulerException
   */
  public void remove(String triggerCode) throws SchedulerException {
    //获得触发器元数据
    TriggerMeta triggerMeta = getTriggerMeta(triggerCode);
    // 获得triggerKey
    TriggerKey triggerKey = getTriggerKey(triggerMeta);
    // 操作
    this.s.getScheduler().unscheduleJob(triggerKey);
  }

  /**
   * 描述 : 暂停
   *
   * @param triggerCode triggerCode
   * @throws SchedulerException SchedulerException
   */
  public void puse(String triggerCode) throws SchedulerException {
    //获得触发器元数据
    TriggerMeta triggerMeta = getTriggerMeta(triggerCode);
    // 获得triggerKey
    TriggerKey triggerKey = getTriggerKey(triggerMeta);
    // 操作
    this.s.getScheduler().pauseTrigger(triggerKey);
  }

  /**
   * 描述 : 恢复
   *
   * @param triggerCode triggerCode
   * @throws SchedulerException SchedulerException
   */
  public void resume(String triggerCode) throws SchedulerException {
    //获得触发器元数据
    TriggerMeta triggerMeta = getTriggerMeta(triggerCode);
    // 获得triggerKey
    TriggerKey triggerKey = getTriggerKey(triggerMeta);
    // 操作
    this.s.getScheduler().resumeTrigger(triggerKey);
  }

  /**
   * 描述 : 获得TriggerKey
   *
   * @param triggerMeta 触发器元数据
   * @return TriggerKey
   * @throws SchedulerException SchedulerException
   */
  protected TriggerKey getTriggerKey(TriggerMeta triggerMeta) throws SchedulerException {
    // 获得计划任务管理器
    Scheduler sch = this.s.getScheduler();
    // 设置triggerKey
    TriggerKey triggerKey = new TriggerKey(triggerMeta.getName(), triggerMeta.getGroup());
    //判断job是否存在
    if (sch.checkExists(triggerKey)) {
      return triggerKey;
    } else {
      throw new SchException("trigger key : " + triggerMeta.getName() + " / group : "
          + triggerMeta.getGroup() + " not exist!");
    }
  }

  /**
   * 描述 : 返回触发器元数据
   *
   * @param triggerCode 触发器代码
   * @return 触发器元数据
   */
  protected TriggerMeta getTriggerMeta(String triggerCode) {
    TriggerMeta triggerMeta = getCronTriggerMeta(triggerCode);
    if (triggerMeta == null) {
      triggerMeta = getSimpleTriggerMeta(triggerCode);
      if (triggerMeta == null) {
        throw new SchException("triggerCode : " + triggerCode + " not defined!"); //NOSONAR
      }
    }
    return triggerMeta;
  }

  /**
   * 描述 : 获得cron触发器元数据
   *
   * @param cronTriggerCode simple触发器代码
   * @return simple触发器元数据
   */
  protected CronTriggerMeta getCronTriggerMeta(String cronTriggerCode) {
    //获得触发器定义
    CronTriggerMeta cronTriggerMeta = schedulerProperties.getCronTrigger().get(cronTriggerCode);
    //判断触发器是否存在
    if (cronTriggerMeta == null) {
      throw new SchException("cronTriggerCode : " + cronTriggerCode + " not defined!"); //NOSONAR
    }
    //获得组别定义
    String groupName = schedulerProperties.getTriggerGroup().get(cronTriggerMeta.getGroup());
    //判断组别是否存在
    if (StringUtils.isBlank(groupName)) {
      throw new SchException("group : " + cronTriggerMeta.getGroup() + " not defined!"); //NOSONAR
    }
    //返回
    return cronTriggerMeta;
  }

  /**
   * 描述 : 获得simple触发器元数据
   *
   * @param simpleTriggerCode simple触发器代码
   * @return simple触发器元数据
   */
  protected SimpleTriggerMeta getSimpleTriggerMeta(String simpleTriggerCode) {
    //获得触发器定义
    SimpleTriggerMeta simpleTriggerMeta =
        schedulerProperties.getSimpleTrigger().get(simpleTriggerCode);
    //判断触发器是否存在
    if (simpleTriggerMeta == null) {
      throw new SchException("simpleTriggerCode : " + simpleTriggerCode + " not defined!"); //NOSONAR
    }
    //获得组别定义
    String groupName = schedulerProperties.getTriggerGroup().get(simpleTriggerMeta.getGroup());
    //判断组别是否存在
    if (StringUtils.isBlank(groupName)) {
      throw new SchException("group : " + simpleTriggerMeta.getGroup() + " not defined!"); //NOSONAR
    }
    //返回
    return simpleTriggerMeta;
  }

}
