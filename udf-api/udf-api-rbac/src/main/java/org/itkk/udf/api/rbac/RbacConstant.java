package org.itkk.udf.api.rbac;

/**
 * 常量类
 */
public class RbacConstant {

    /**
     * 用户注册缓存key
     */
    public static final String USER_KEY_PREFIX = "USER_";

    /**
     * 用户状态(正常)
     */
    public enum USER_STATUS {

        /**
         * 正常
         */
        STATUS_1(1);

        private Integer value;

        USER_STATUS(Integer value) {
            this.value = value;
        }

        public Integer value() {
            return this.value;
        }

    }

    /**
     * 私有化构造函数
     */
    private RbacConstant() {

    }
}
