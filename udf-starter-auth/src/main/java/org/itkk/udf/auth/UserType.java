package org.itkk.udf.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述 : UserType
 *
 * @author wangkang
 */
public enum UserType {

    /**
     * 描述 : 用户
     */
    USER,
    /**
     * 描述 : 客户端
     */
    CLIENT;

    /**
     * mappings
     */
    private static final Map<String, UserType> mappings = new HashMap<>();

    /**
     * 静态块
     */
    static {
        for (UserType userType : values()) {
            mappings.put(userType.name(), userType);
        }
    }

    /**
     * 转换为枚举类型
     *
     * @param userType 用户类型
     * @return 枚举
     */
    public static UserType resolve(String userType) {
        return (userType != null ? mappings.get(userType) : null);
    }

    /**
     * 匹配
     *
     * @param userType 用户类型
     * @return 匹配结果
     */
    public boolean matches(String userType) {
        return (this == resolve(userType));
    }

}
