package org.itkk.udf.weixin.mp.api.meta;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * WeixinMpApiAuthMeta
 */
@Data
public class WeixinMpApiAuthMeta implements Serializable {

    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 开发者ID
     */
    private String appId;

    /**
     * 开发者密码
     */
    private String appSecret;

    /**
     * 令牌
     */
    private String token;

    /**
     * 消息加解密密钥
     */
    private String encodingAesKey;

    /**
     * 是否安全模式(密文消息)
     */
    private Boolean safeModel = true;

    /**
     * 模板消息的模板ID
     */
    private Map<String, String> messageTemplateId;

}
