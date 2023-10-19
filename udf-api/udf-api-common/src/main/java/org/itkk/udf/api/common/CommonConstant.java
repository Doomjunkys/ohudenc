package org.itkk.udf.api.common;

/**
 * 常量类
 */
public class CommonConstant {

    /**
     * web根路径
     */
    public static final String URL_ROOT_WEB = "/api/udf/web";

    /**
     * 需认证的web根路径
     */
    public static final String URL_ROOT_WEB_PRIVATE = URL_ROOT_WEB + "/private/";

    /**
     * 无需认证的web根路径
     */
    public static final String URL_ROOT_WEB_PUBLIC = URL_ROOT_WEB + "/public/";

    /**
     * 默认时间格式
     */
    public static final String DEF_DATA_FMT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认锁失败信息
     */
    public static final String DEF_LOCK_FAIL_MSG = "该数据正在被处理中,请稍后再试";

    /**
     * token参数名
     */
    public static final String PARAMETER_NAME_TOKEN = "token";

    /**
     * token信息参数名
     */
    public static final String PARAMETER_NAME_TOKEN_INFO = "tokenInfo";

    /**
     * token超时时间
     */
    public static final long TOKEN_CACHE_TTL = 30 * 60 * 1000L;

    /**
     * 刷新token时间
     */
    public static final long REFRESH_TOKEN_CACHE_TTL = 15 * 60 * 1000L;

    /**
     * 锁超时时间
     */
    public static final Long LOCK_EXPAIR = 5 * 60 * 1000L;

    /**
     * 发送邮件队列名
     */
    public static final String SEND_EMAIL_QUEUE = "SEND_EMAIL_QUEUE";

    /**
     * 发送邮件队列处理器
     */
    public static final String SEND_EMAIL_QUEUE_HANDLE = "SEND_EMAIL_QUEUE_HANDLE";

    /**
     * 数据状态(正常,删除)
     */
    public enum DATA_STATUS {

        /**
         * 删除
         */
        STATUS_F1(-1),

        /**
         * 正常
         */
        STATUS_1(1);

        private Integer value;

        DATA_STATUS(Integer value) {
            this.value = value;
        }

        public Integer value() {
            return this.value;
        }

    }

    /**
     * 私有化构造函数
     */
    private CommonConstant() {

    }
}
