package org.itkk.udf.auth;

import lombok.Data;
import org.itkk.udf.auth.meta.ExcludeServiceMeta;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * AuthProperties
 */
@Component
@Data
@ConfigurationProperties(prefix = "org.itkk.auth.properties")
public class AuthProperties {

    /**
     * 是否单例登录
     */
    private boolean singletonLogin = false;

    /**
     * 用户服务排除
     */
    private Map<String, ExcludeServiceMeta> userExcludeService;

    /**
     * 客户端服务排除
     */
    private Map<String, ExcludeServiceMeta> clientExcludeServiceMeta;

    /**
     * 构造方法
     */
    public AuthProperties() {
        userExcludeService = new HashMap<>();
        ExcludeServiceMeta authentication = new ExcludeServiceMeta();
        authentication.setMethod(Constant.USER_AUTHENTICATION_METHOD);
        authentication.setUrl(Constant.USER_AUTHENTICATION_URL);
        userExcludeService.put("AUTH_USER_1", authentication);
    }

}
