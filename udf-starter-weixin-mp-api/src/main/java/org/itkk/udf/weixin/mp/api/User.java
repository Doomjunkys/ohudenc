package org.itkk.udf.weixin.mp.api;

import org.itkk.udf.weixin.mp.api.domain.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * User
 */
@Component
public class User {

    /**
     * token
     */
    @Autowired
    private Token token;

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
     * 获取用户基本信息（包括UnionID机制）
     *
     * @param businessCode 业务代码
     * @param openid       普通用户的标识，对当前公众号唯一
     * @param lang         返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return UserInfo
     */
    public UserInfo info(String businessCode, String openid, String lang) {
        //获得接口地址
        String path = weixinMpApiProperties.getApiPath().get("user_info");
        //拼接url &openid=OPENID&lang=zh_CN
        StringBuilder url = new StringBuilder(path);
        url.append(WeixinMpConsont.appendAccessToken(token.get(businessCode))).append("&openid=").append(openid).append("&lang=").append(lang);
        //请求
        UserInfo result = externalRestTemplate.getForEntity(url.toString(), UserInfo.class).getBody();
        //验证
        WeixinMpConsont.checkResult(result);
        //返回
        return result;
    }

}
