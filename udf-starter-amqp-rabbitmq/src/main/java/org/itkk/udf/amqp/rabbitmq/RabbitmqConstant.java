/**
 * BaseMqConstant.java
 * Created at 2016-12-05
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package org.itkk.udf.amqp.rabbitmq;

/**
 * 描述 : BaseMqConstant
 *
 * @author Administrator
 */
public class RabbitmqConstant {

    /**
     * 描述 : X_DEAD_LETTER_EXCHANGE
     */
    public static final String X_DEAD_LETTER_EXCHANGE = "x-dead-letter-exchange";

    /**
     * 描述 : X_DEAD_LETTER_ROUTING_KEY
     */
    public static final String X_DEAD_LETTER_ROUTING_KEY = "x-dead-letter-routing-key";

    /**
     * (EXCHANGE)消息记录
     */
    public enum EXCHANGE_MESSAGE_LOG {

    }

    /**
     * (EXCHANGE)消息记录(死信)
     */
    public enum EXCHANGE_MESSAGE_LOG_DLX {

    }

    /**
     * 描述 : 私有化构造函数
     */
    private RabbitmqConstant() {
    }

}
