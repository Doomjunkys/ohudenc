/**
 * IExecutor.java
 * Created at 2017-06-11
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.client;

import java.util.Map;

/**
 * 描述 : IExecutor
 *
 * @author Administrator
 */
public interface IExecutor { //NOSONAR

    /**
     * 描述 : 执行方法
     *
     * @param jobDataMap jobDataMap
     */
    void handle(Map<String, Object> jobDataMap);

}
