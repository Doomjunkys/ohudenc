package org.itkk.udf.starter.ws;

import lombok.extern.slf4j.Slf4j;

/**
 * 常量
 */
@Slf4j
public class WsConstant {

    /**
     * 服务器推送默认路由名称
     */
    public static final String DEF_SERVER_PUSH_ROUTING_NAME = "serverPush";

    /**
     * 发送的url
     */
    public static final String SEND_URL = "/send";

    /**
     * 类型名称键
     */
    protected static final String TYPE_NAME = "type";

    /**
     * 令牌名称键
     */
    protected static final String TOKEN_NAME = "token";

    /**
     * 用户名称键
     */
    protected static final String USER_ID_NAME = "userId";

    /**
     * 响应状态
     */
    public enum WS_RESPONSE_STATUS {

        /**
         * 成功
         */
        SUCCESS("1"),
        /**
         * 失败
         */
        FIAL("-1");


        private String value;

        WS_RESPONSE_STATUS(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * 私有化构造函数
     */
    private WsConstant() {

    }
}
