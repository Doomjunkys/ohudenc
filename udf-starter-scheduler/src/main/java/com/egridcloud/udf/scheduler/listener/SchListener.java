/**
 * SchListener.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.SchedulerListener;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.egridcloud.udf.scheduler.IListenerLog;

/**
 * <p>
 * ClassName: SchedulerListener
 * </p>
 * <p>
 * Description: 计划任务监听器
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2014年1月24日
 * </p>
 */
public class SchListener implements SchedulerListener {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(SchListener.class);

  /**
   * <p>
   * Field CHAR_1: 斜杠
   * </p>
   */
  private static final String CHAR_1 = "/";

  /**
   * <p>
   * Field ZHUOYE: 作业
   * </p>
   */
  private static final String ZHUOYE = "作业";

  /**
   * 描述 : listenerLog
   */
  @Autowired(required = false)
  private IListenerLog listenerLog;

  @Override
  public void triggerPaused(TriggerKey triggerKey) {
    String log = "(triggerPaused)" + triggerKey.getName() + "/" + triggerKey.getGroup() + "被暂停了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void triggerResumed(TriggerKey triggerKey) {
    String log =
        "(triggerResumed)" + triggerKey.getName() + CHAR_1 + triggerKey.getGroup() + "被恢复了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void jobScheduled(Trigger trigger) {
    String log = "(jobScheduled)" + ZHUOYE + trigger.getJobKey().getName() + CHAR_1
        + trigger.getJobKey().getGroup() + "被触发器" + trigger.getKey().getName() + CHAR_1
        + trigger.getKey().getGroup() + "触发了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void jobUnscheduled(TriggerKey triggerKey) {
    String log =
        "(jobUnscheduled)" + triggerKey.getName() + CHAR_1 + triggerKey.getGroup() + "被移除了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void triggerFinalized(Trigger trigger) {
    String log = "(triggerFinalized)" + ZHUOYE + trigger.getJobKey().getName() + CHAR_1
        + trigger.getJobKey().getGroup() + ",触发器" + trigger.getKey().getName() + CHAR_1
        + trigger.getKey().getGroup() + "已经执行完成,后续将不会继续触发";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void triggersPaused(String triggerGroup) {
    if (StringUtils.isEmpty(triggerGroup)) {
      String log = "(triggersPaused)" + "触发器组全部被暂停了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    } else {
      String log = "(triggersPaused)" + "触发器组" + triggerGroup + "被暂停了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    }

  }

  @Override
  public void triggersResumed(String triggerGroup) {
    if (StringUtils.isEmpty(triggerGroup)) {
      String log = "(triggersResumed)" + "触发器组全部被恢复了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    } else {
      String log = "(triggersResumed)" + "触发器组" + triggerGroup + "被恢复了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    }
  }

  @Override
  public void jobAdded(JobDetail jobDetail) {
    String log = "(jobAdded)" + ZHUOYE + jobDetail.getKey().getName() + CHAR_1
        + jobDetail.getKey().getGroup() + "被添加了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void jobDeleted(JobKey jobKey) {
    String log = "(jobDeleted)" + ZHUOYE + jobKey.getName() + CHAR_1 + jobKey.getGroup() + "被删除了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void jobPaused(JobKey jobKey) {
    String log = "(jobPaused)" + ZHUOYE + jobKey.getName() + CHAR_1 + jobKey.getGroup() + "被暂停了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void jobsPaused(String jobGroup) {
    if (StringUtils.isEmpty(jobGroup)) {
      String log = "(jobsPaused)" + "作业全部被暂停了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    } else {
      String log = "(jobsPaused)" + "作业组" + jobGroup + "被暂停了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    }

  }

  @Override
  public void jobResumed(JobKey jobKey) {
    String log = "(jobResumed)" + ZHUOYE + jobKey.getName() + CHAR_1 + jobKey.getGroup() + "被恢复了";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void jobsResumed(String jobGroup) {
    if (StringUtils.isEmpty(jobGroup)) {
      String log = "(jobsResumed)" + "作业全部被恢复了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    } else {
      String log = "(jobsResumed)" + "作业组" + jobGroup + "被恢复了";
      LOGGER.info(log);
      if (listenerLog != null) {
        listenerLog.save(log);
      }
    }
  }

  @Override
  public void schedulerError(String msg, SchedulerException cause) {
    // 获得异常详细信息
    String exceptionDetail = ExceptionUtils.getStackTrace(cause);
    String log = "(schedulerError)" + "计划任务出错:" + msg + "\n" + exceptionDetail;
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void schedulerInStandbyMode() {
    String log = "(schedulerInStandbyMode)" + "计划任务为待机状态";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void schedulerStarted() {
    String log = "(schedulerStarted)" + "计划任务已经启动";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void schedulerStarting() {
    String log = "(schedulerStarting)" + "计划任务正在启动中";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void schedulerShutdown() {
    String log = "(schedulerShutdown)" + "计划任务已关闭";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void schedulerShuttingdown() {
    String log = "(schedulerShuttingdown)" + "计划任务正在关闭中";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }

  @Override
  public void schedulingDataCleared() {
    String log = "(schedulingDataCleared)" + "计划任务数据被清除";
    LOGGER.info(log);
    if (listenerLog != null) {
      listenerLog.save(log);
    }
  }
}
