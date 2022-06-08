/**
 * IRmsJobEvent.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler;

import org.itkk.udf.scheduler.client.domain.RmsJobParam;
import org.itkk.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : IRmsJobEvent
 *
 * @author Administrator
 */
public interface IRmsJobEvent {

    /**
     * hasRunning
     *
     * @param rmsJobParam rmsJobParam
     * @return boolean
     */
    boolean hasRunning(RmsJobParam rmsJobParam);

    /**
     * 描述 : save
     *
     * @param param  param
     * @param result result
     */
    void save(RmsJobParam param, RmsJobResult result);

    /**
     * 描述 : delete
     *
     * @param fireInstanceId fireInstanceId
     */
    void delete(String fireInstanceId);

}
