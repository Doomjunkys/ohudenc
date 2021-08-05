/**
 * ApplicationConfig.java
 * Created at 2016-10-03
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.core;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 描述 : 系统配置
 *
 * @author wangkang
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.application.config")
@Validated
public class ApplicationConfig {

  /**
   * 描述 : 是否输出异常堆栈
   */
  @NotNull
  private boolean outputExceptionStackTrace = false;

  /**
   * 描述 : 编码
   */
  @NotNull
  private String encoding = "UTF-8";

  /**
   * 描述 : 时区
   */
  @NotNull
  private String timeZone = "Asia/Shanghai";

  /**
   * 描述 : 日期格式
   */
  @NotNull
  private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss:SSSZ";

  /**
   * 描述 : 线程池属性
   */
  private TaskExecutePoolProperties taskExecutePoolProperties;

  /**
   * 描述 : 获取timeZone
   *
   * @return the timeZone
   */
  public String getTimeZone() {
    return timeZone;
  }

  /**
   * 描述 : 设置timeZone
   *
   * @param timeZone the timeZone to set
   */
  public void setTimeZone(String timeZone) {
    this.timeZone = timeZone;
  }

  /**
   * 描述 : 获取encoding
   *
   * @return the encoding
   */
  public String getEncoding() {
    return encoding;
  }

  /**
   * 描述 : 设置encoding
   *
   * @param encoding the encoding to set
   */
  public void setEncoding(String encoding) {
    this.encoding = encoding;
  }

  /**
   * 描述 : 获取dateFormat
   *
   * @return the dateFormat
   */
  public String getDateFormat() {
    return dateFormat;
  }

  /**
   * 描述 : 设置dateFormat
   *
   * @param dateFormat the dateFormat to set
   */
  public void setDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
  }

  /**
   * 描述 : 获取outputExceptionStackTrace
   *
   * @return the outputExceptionStackTrace
   */
  public boolean isOutputExceptionStackTrace() {
    return outputExceptionStackTrace;
  }

  /**
   * 描述 : 设置outputExceptionStackTrace
   *
   * @param outputExceptionStackTrace the outputExceptionStackTrace to set
   */
  public void setOutputExceptionStackTrace(boolean outputExceptionStackTrace) {
    this.outputExceptionStackTrace = outputExceptionStackTrace;
  }

  /**
   * 描述 : 获取taskExecutePoolProperties
   *
   * @return the taskExecutePoolProperties
   */
  public TaskExecutePoolProperties getTaskExecutePoolProperties() {
    return taskExecutePoolProperties;
  }

  /**
   * 描述 : 设置taskExecutePoolProperties
   *
   * @param taskExecutePoolProperties the taskExecutePoolProperties to set
   */
  public void setTaskExecutePoolProperties(TaskExecutePoolProperties taskExecutePoolProperties) {
    this.taskExecutePoolProperties = taskExecutePoolProperties;
  }

  /**
   * 描述 : 线程池配置
   *
   * @author Administrator
   *
   */
  public static class TaskExecutePoolProperties {

    /**
     * 描述 : 常量
     */
    private final Integer keepAliveSecondsValue = 60;

    /**
     * 描述 : 默认线程池大小(默认:1)
     */
    private Integer corePoolSize = 1;

    /**
     * 描述 : 最大线程池大小(默认:Integer.MAX_VALUE)
     */
    private Integer maxPoolSize = Integer.MAX_VALUE;

    /**
     * 描述 : 队列容量(默认:Integer.MAX_VALUE)
     */
    private Integer queueCapacity = Integer.MAX_VALUE;

    /**
     * 描述 : 线程池存活时间
     */
    private Integer keepAliveSeconds = keepAliveSecondsValue;

    /**
     * 描述 : 线程优先级
     */
    private Integer threadPriority = Thread.NORM_PRIORITY;

    /**
     * 描述 : 线程名称
     */
    private String threadNamePrefix = "defaultTaskAsyncPool";

    /**
     * 描述 : 获取corePoolSize
     *
     * @return the corePoolSize
     */
    public Integer getCorePoolSize() {
      return corePoolSize;
    }

    /**
     * 描述 : 设置corePoolSize
     *
     * @param corePoolSize the corePoolSize to set
     */
    public void setCorePoolSize(Integer corePoolSize) {
      this.corePoolSize = corePoolSize;
    }

    /**
     * 描述 : 获取maxPoolSize
     *
     * @return the maxPoolSize
     */
    public Integer getMaxPoolSize() {
      return maxPoolSize;
    }

    /**
     * 描述 : 设置maxPoolSize
     *
     * @param maxPoolSize the maxPoolSize to set
     */
    public void setMaxPoolSize(Integer maxPoolSize) {
      this.maxPoolSize = maxPoolSize;
    }

    /**
     * 描述 : 获取queueCapacity
     *
     * @return the queueCapacity
     */
    public Integer getQueueCapacity() {
      return queueCapacity;
    }

    /**
     * 描述 : 设置queueCapacity
     *
     * @param queueCapacity the queueCapacity to set
     */
    public void setQueueCapacity(Integer queueCapacity) {
      this.queueCapacity = queueCapacity;
    }

    /**
     * 描述 : 获取keepAliveSeconds
     *
     * @return the keepAliveSeconds
     */
    public Integer getKeepAliveSeconds() {
      return keepAliveSeconds;
    }

    /**
     * 描述 : 设置keepAliveSeconds
     *
     * @param keepAliveSeconds the keepAliveSeconds to set
     */
    public void setKeepAliveSeconds(Integer keepAliveSeconds) {
      this.keepAliveSeconds = keepAliveSeconds;
    }

    /**
     * 描述 : 获取threadPriority
     *
     * @return the threadPriority
     */
    public Integer getThreadPriority() {
      return threadPriority;
    }

    /**
     * 描述 : 设置threadPriority
     *
     * @param threadPriority the threadPriority to set
     */
    public void setThreadPriority(Integer threadPriority) {
      this.threadPriority = threadPriority;
    }

    /**
     * 描述 : 获取threadNamePrefix
     *
     * @return the threadNamePrefix
     */
    public String getThreadNamePrefix() {
      return threadNamePrefix;
    }

    /**
     * 描述 : 设置threadNamePrefix
     *
     * @param threadNamePrefix the threadNamePrefix to set
     */
    public void setThreadNamePrefix(String threadNamePrefix) {
      this.threadNamePrefix = threadNamePrefix;
    }

  }

}
