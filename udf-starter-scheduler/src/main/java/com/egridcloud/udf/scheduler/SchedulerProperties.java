/**
 * SchedulerProperties.java
 * Created at 2017-06-01
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述 : SchedulerProperties
 *
 * @author Administrator
 *
 */
@Component
@ConfigurationProperties(prefix = "com.egridcloud.scheduler.properties")
public class SchedulerProperties {

}
