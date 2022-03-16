/**
 * RmsJobResult.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.client.domain;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 描述 : RmsJobResult
 *
 * @author Administrator
 */
@ApiModel(description = "通用job返回值")
public class RmsJobResult implements Serializable {

    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 描述 : 主键ID
     */
    @ApiModelProperty(value = "主键ID", required = true, dataType = "string")
    private String id;

    /**
     * 描述 : 客户端接收时间
     */
    @ApiModelProperty(value = "客户端接收时间", required = true, dataType = "long")
    private Date clientReceiveTime;

    /**
     * 描述 : 客户端开始时间
     */
    @ApiModelProperty(value = "客户端开始时间", required = true, dataType = "long")
    private Date clientStartExecuteTime;

    /**
     * 描述 : 客户端结束时间
     */
    @ApiModelProperty(value = "客户端结束时间", required = false, dataType = "long")
    private Date clientEndExecuteTime;

    /**
     * 描述 : 状态
     */
    @ApiModelProperty(value = "状态", required = true, dataType = "int")
    private Integer stats;

    /**
     * 描述 : 错误信息
     */
    @ApiModelProperty(value = "错误信息", required = true, dataType = "string")
    private String errorMsg;

    /**
     * 描述 : 获取errorMsg
     *
     * @return the errorMsg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * 描述 : 设置errorMsg
     *
     * @param errorMsg the errorMsg to set
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    /**
     * 描述 : 获取stats
     *
     * @return the stats
     */
    public Integer getStats() {
        return stats;
    }

    /**
     * 描述 : 设置stats
     *
     * @param stats the stats to set
     */
    public void setStats(Integer stats) {
        this.stats = stats;
    }

    /**
     * 描述 : 获取clientReceiveTime
     *
     * @return the clientReceiveTime
     */
    public Date getClientReceiveTime() {
        return clientReceiveTime;
    }

    /**
     * 描述 : 设置clientReceiveTime
     *
     * @param clientReceiveTime the clientReceiveTime to set
     */
    public void setClientReceiveTime(Date clientReceiveTime) {
        this.clientReceiveTime = clientReceiveTime;
    }

    /**
     * 描述 : 获取clientStartExecuteTime
     *
     * @return the clientStartExecuteTime
     */
    public Date getClientStartExecuteTime() {
        return clientStartExecuteTime;
    }

    /**
     * 描述 : 设置clientStartExecuteTime
     *
     * @param clientStartExecuteTime the clientStartExecuteTime to set
     */
    public void setClientStartExecuteTime(Date clientStartExecuteTime) {
        this.clientStartExecuteTime = clientStartExecuteTime;
    }

    /**
     * 描述 : 获取clientEndExecuteTime
     *
     * @return the clientEndExecuteTime
     */
    public Date getClientEndExecuteTime() {
        return clientEndExecuteTime;
    }

    /**
     * 描述 : 设置clientEndExecuteTime
     *
     * @param clientEndExecuteTime the clientEndExecuteTime to set
     */
    public void setClientEndExecuteTime(Date clientEndExecuteTime) {
        this.clientEndExecuteTime = clientEndExecuteTime;
    }

    /**
     * 描述 : 获取id
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * 描述 : 设置id
     *
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
