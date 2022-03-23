/**
 * TriggerMeta.java
 * Created at 2017-06-02
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.meta;

import org.quartz.Trigger;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 描述 : TriggerMeta.java
 *
 * @author Administrator
 */
public class TriggerMeta implements Serializable {

    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 描述 : 作业代码
     */
    private String jobCode;

    /**
     * 描述 : 名称
     */
    private String name;

    /**
     * 描述 : 组别
     */
    private String group = "default";

    /**
     * 描述 : 描述
     */
    private String description = "暂无描述";

    /**
     * 描述 : 开始时间
     */
    private Date startTime;

    /**
     * 描述 : 结束时间
     */
    private Date endTime;

    /**
     * 描述 : 优先级
     */
    private Integer priority = 0;

    /**
     * 描述 : 日历
     */
    private String calendar;

    /**
     * 描述 : 遗漏对应的策略
     */
    private Integer misfireInstruction = Trigger.MISFIRE_INSTRUCTION_SMART_POLICY;

    /**
     * 是否自动实例化
     */
    private Boolean autoInit = false;

    /**
     * 描述 : 数据
     */
    private Map<String, String> dataMap;

    /**
     * getAutoInit
     *
     * @return Boolean
     */
    public Boolean getAutoInit() {
        return autoInit;
    }

    /**
     * setAutoInit
     *
     * @param autoInit autoInit
     */
    public void setAutoInit(Boolean autoInit) {
        this.autoInit = autoInit;
    }

    /**
     * 描述 : 获取misfireInstruction
     *
     * @return the misfireInstruction
     */
    public Integer getMisfireInstruction() {
        return misfireInstruction;
    }

    /**
     * 描述 : 设置misfireInstruction
     *
     * @param misfireInstruction the misfireInstruction to set
     */
    public void setMisfireInstruction(Integer misfireInstruction) {
        this.misfireInstruction = misfireInstruction;
    }

    /**
     * 描述 : 获取jobCode
     *
     * @return the jobCode
     */
    public String getJobCode() {
        return jobCode;
    }

    /**
     * 描述 : 设置jobCode
     *
     * @param jobCode the jobCode to set
     */
    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    /**
     * 描述 : 获取name
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 描述 : 设置name
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 描述 : 获取group
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }

    /**
     * 描述 : 设置group
     *
     * @param group the group to set
     */
    public void setGroup(String group) {
        this.group = group;
    }

    /**
     * 描述 : 获取description
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 描述 : 设置description
     *
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 描述 : 获取startTime
     *
     * @return the startTime
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 描述 : 设置startTime
     *
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 描述 : 获取endTime
     *
     * @return the endTime
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 描述 : 设置endTime
     *
     * @param endTime the endTime to set
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 描述 : 获取priority
     *
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * 描述 : 设置priority
     *
     * @param priority the priority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * 描述 : 获取calendar
     *
     * @return the calendar
     */
    public String getCalendar() {
        return calendar;
    }

    /**
     * 描述 : 设置calendar
     *
     * @param calendar the calendar to set
     */
    public void setCalendar(String calendar) {
        this.calendar = calendar;
    }

    /**
     * 描述 : 获取dataMap
     *
     * @return the dataMap
     */
    public Map<String, String> getDataMap() {
        return dataMap;
    }

    /**
     * 描述 : 设置dataMap
     *
     * @param dataMap the dataMap to set
     */
    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

}
