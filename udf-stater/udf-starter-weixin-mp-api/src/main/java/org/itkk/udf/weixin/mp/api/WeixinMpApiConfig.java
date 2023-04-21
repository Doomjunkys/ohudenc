package org.itkk.udf.weixin.mp.api;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * WeixinMpApiConfig
 */
@Configuration
public class WeixinMpApiConfig {

    /**
     * EXCHANGE_WEIXIN_MP_MESSAGE
     */
    public static final String EXCHANGE_WEIXIN_MP_MESSAGE = "EXCHANGE_WEIXIN_MP_MESSAGE";

    /**
     * EXCHANGE_WEIXIN_MP_EVENT
     */
    public static final String EXCHANGE_WEIXIN_MP_EVENT = "EXCHANGE_WEIXIN_MP_EVENT";

    /**
     * exchangeWeixinMpMessage
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchangeWeixinMpMessage() {
        return new DirectExchange(EXCHANGE_WEIXIN_MP_MESSAGE, true, false);
    }

    /**
     * exchangeWeixinMpMessageDlx
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchangeWeixinMpMessageDlx() {
        return new DirectExchange(EXCHANGE_WEIXIN_MP_MESSAGE + "_DLX", true, false);
    }

    /**
     * exchangeWeixinMpEvent
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchangeWeixinMpEvent() {
        return new DirectExchange(EXCHANGE_WEIXIN_MP_EVENT, true, false);
    }

    /**
     * exchangeWeixinMpEventDlx
     *
     * @return DirectExchange
     */
    @Bean
    public DirectExchange exchangeWeixinMpEventDlx() {
        return new DirectExchange(EXCHANGE_WEIXIN_MP_EVENT + "_DLX", true, false);
    }

}
