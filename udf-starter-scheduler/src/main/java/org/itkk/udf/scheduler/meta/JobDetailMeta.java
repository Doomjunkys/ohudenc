/**
 * JobDetailMeta.java
 * Created at 2017-06-02
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.meta;

import java.io.Serializable;
import java.util.Map;

/**
 * 描述 : JobDetailMeta
 *
 * @author Administrator
 */
public class JobDetailMeta implements Serializable {

    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 描述 : 名称
     */
    private String name;

    /**
     * 描述 : 组别
     */
    private String group = "default";

    /**
     * 描述 : 类型
     */
    private String className;

    /**
     * 描述 : 描述
     */
    private String description = "暂无描述";

    /**
     * 描述 : 是否可恢复
     */
    private Boolean recovery = false;

    /**
     * 描述 : 是否持久化
     */
    private Boolean durability = true;

    /**
     * 描述 : 数据
     */
    private Map<String, String> dataMap;

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
     * 描述 : 获取className
     *
     * @return the className
     */
    public String getClassName() {
        return className;
    }

    /**
     * 描述 : 设置className
     *
     * @param className the className to set
     */
    public void setClassName(String className) {
        this.className = className;
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
     * 描述 : 获取recovery
     *
     * @return the recovery
     */
    public Boolean getRecovery() {
        return recovery;
    }

    /**
     * 描述 : 设置recovery
     *
     * @param recovery the recovery to set
     */
    public void setRecovery(Boolean recovery) {
        this.recovery = recovery;
    }

    /**
     * 描述 : 获取durability
     *
     * @return the durability
     */
    public Boolean getDurability() {
        return durability;
    }

    /**
     * 描述 : 设置durability
     *
     * @param durability the durability to set
     */
    public void setDurability(Boolean durability) {
        this.durability = durability;
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
