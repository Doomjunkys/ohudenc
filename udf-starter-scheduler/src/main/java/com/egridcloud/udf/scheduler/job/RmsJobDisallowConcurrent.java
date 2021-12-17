/**
 * RmsJobDisallowConcurrent.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.scheduler.job;

import org.quartz.DisallowConcurrentExecution;

/**
 * 描述 : RmsJobDisallowConcurrent
 *
 * @author Administrator
 *
 */
@DisallowConcurrentExecution
public class RmsJobDisallowConcurrent extends RmsJob {

}
