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
     * rbac 角色缓存key前缀
     */
    public static final String RBAC_ROLE_KEY_PREFIX = "rbac_role_";

    /**
     * rbac 用户缓存key前缀
     */
    public static final String RBAC_USER_KEY_PREFIX = "rbac_user_";

    /**
     * rbac 数据缓存key前缀
     */
    public static final String RBAC_CACHE_KEY_PREFIX = "rbac_";

    /**
     * ROOT菜单代码
     */
    public static final String ROOT_MENU_CODE = "-1";

    /**
     * 角色状态
     */
    public enum ROLE_STATUS {

        /**
         * 作废
         */
        STATUS_F1(-1),

        /**
         * 启用
         */
        STATUS_1(1);

        private Integer value;

        ROLE_STATUS(Integer value) {
            this.value = value;
        }

        public Integer value() {
            return this.value;
        }

    }

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
