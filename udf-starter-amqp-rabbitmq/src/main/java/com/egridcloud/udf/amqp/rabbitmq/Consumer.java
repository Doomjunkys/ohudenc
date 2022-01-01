/**
 * Consumer.java
 * Created at 2017-05-23
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.amqp.rabbitmq;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述 : Consumer
 *
 * @author Administrator
 *
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Consumer {

  /**
   * 描述 : 消费者代码
   *
   * @return value
   */
  String value() default "";

}
