/**
 * RmsJobDisallowConcurrent.java
 * Created at 2017-06-04
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.scheduler.job;

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
