/**
 * IRmsJobLog.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler;

import org.itkk.udf.scheduler.client.domain.RmsJobResult;

/**
 * 描述 : IRmsJobLog
 *
 * @author Administrator
 */
public interface IRmsJobLog {

    /**
     * 描述 : save
     *
     * @param result result
     */
    void save(RmsJobResult result);

    /**
     * 描述 : delete
     *
     * @param fireInstanceId fireInstanceId
     */
    void delete(String fireInstanceId);

}
