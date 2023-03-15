package org.itkk.udf.weixin.mp.api;

import org.itkk.udf.weixin.mp.api.domain.AccessToken;
import org.itkk.udf.weixin.mp.api.meta.WeixinMpApiAuthMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Token
 */
@Component
public class Token {

    /**
     * DISTRIBUTED_LOCK_KEY_PREFIX
     */
    private static final String WEIXIN_MP_TOKEN_KEY_PREFIX = ":WEIXIN_MP_TOKEN:";

    /**
     * weixinMpApiProperties
     */
    @Autowired
    private WeixinMpApiProperties weixinMpApiProperties;

    /**
     * externalRestTemplate
     */
    @Autowired
    @Qualifier("externalRestTemplate")
    private RestTemplate externalRestTemplate;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * profiles
     */
    @Value("${spring.profiles.active}")
    private String profiles;

    /**
     * 获取access_token
     *
     * @param businessCode 业务代码
     * @return AccessToken
     */
    protected String get(String businessCode) {
        //获得认证相关信息
        WeixinMpApiAuthMeta auth = weixinMpApiProperties.getAuth().get(businessCode);
        //生成缓存key
        String key = profiles + WEIXIN_MP_TOKEN_KEY_PREFIX + businessCode + ":" + auth.getAppId();
        //检查缓存是否存在,如果存在,则直接返回
        if (redisTemplate.hasKey(key)) {
            return (String) redisTemplate.opsForValue().get(key);
        }
        //获得接口地址
        String path = weixinMpApiProperties.getApiPath().get("token");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append("?appid=").append(auth.getAppId()).append("&secret=").append(auth.getAppSecret()).append("&grant_type=client_credential");
        //请求
        AccessToken result = externalRestTemplate.getForEntity(url.toString(), AccessToken.class).getBody();
        //验证
        WeixinMpConsont.checkResult(result);
        //缓存(新增 & 更新)
        final int num200 = 200;
        int expiresIn = result.getExpires_in() - num200;
        redisTemplate.opsForValue().set(key, result.getAccess_token(), expiresIn, TimeUnit.SECONDS);
        //返回
        return result.getAccess_token();
    }

}
