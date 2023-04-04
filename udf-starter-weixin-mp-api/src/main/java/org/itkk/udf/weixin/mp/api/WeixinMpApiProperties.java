package org.itkk.udf.weixin.mp.api;

import lombok.Data;
import org.itkk.udf.weixin.mp.api.meta.WeixinMpApiAuthMeta;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * WeixinMpApiProperties
 */
@Component
@ConfigurationProperties(prefix = "org.itkk.weixin.mp.api.properties")
@Data
public class WeixinMpApiProperties {

    /**
     * 微信MP认证信息
     */
    private Map<String, WeixinMpApiAuthMeta> auth;

    /**
     * sns接口地址
     */
    private Map<String, String> snsPath;

    /**
     * 接口地址
     */
    private Map<String, String> apiPath;

    /**
     * 模板消息的模板ID
     */
    private Map<String, String> messageTemplateId;

}
