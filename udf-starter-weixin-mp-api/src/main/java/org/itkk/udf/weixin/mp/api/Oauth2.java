package org.itkk.udf.weixin.mp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itkk.udf.weixin.mp.api.domain.BaseResult;
import org.itkk.udf.weixin.mp.api.domain.Oauth2Token;
import org.itkk.udf.weixin.mp.api.domain.Oauth2UserInfo;
import org.itkk.udf.weixin.mp.api.meta.WeixinMpApiAuthMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * Oauth2
 */
@Component
public class Oauth2 {

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
     * objectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 通过code换取网页授权access_token
     *
     * @param businessCode 业务代码(关联appid等信息)
     * @param code         填写第一步获取的code参数
     * @return Oauth2Token
     * @throws IOException IOException
     */
    public Oauth2Token accessToken(String businessCode, String code) throws IOException {
        //获得认证相关信息
        WeixinMpApiAuthMeta auth = weixinMpApiProperties.getAuth().get(businessCode);
        //获得接口地址
        String path = weixinMpApiProperties.getSnsPath().get("sns_oauth2_access_token");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append("?appid=").append(auth.getAppId()).append("&secret=").append(auth.getAppSecret()).append("&code=").append(code).append("&grant_type=authorization_code");
        //请求
        ResponseEntity<String> result = externalRestTemplate.getForEntity(url.toString(), String.class);
        //解析结果
        Oauth2Token token = objectMapper.readValue(result.getBody(), Oauth2Token.class);
        //验证
        WeixinMpConsont.checkResult(token);
        //返回
        return token;
    }

    /**
     * 刷新access_token（如果需要）
     *
     * @param businessCode 业务代码(关联appid等信息)
     * @param refreshToken 填写为refresh_token
     * @return Oauth2Token
     * @throws IOException IOException
     */
    public Oauth2Token refreshToken(String businessCode, String refreshToken) throws IOException {
        //获得认证相关信息
        WeixinMpApiAuthMeta auth = weixinMpApiProperties.getAuth().get(businessCode);
        //获得接口地址
        String path = weixinMpApiProperties.getSnsPath().get("sns_oauth2_refresh_token");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append("?appid=").append(auth.getAppId()).append("&refresh_token=").append(refreshToken).append("&grant_type=refresh_token");
        //请求
        ResponseEntity<String> result = externalRestTemplate.getForEntity(url.toString(), String.class);
        //解析结果
        Oauth2Token token = objectMapper.readValue(result.getBody(), Oauth2Token.class);
        //验证
        WeixinMpConsont.checkResult(token);
        //返回
        return token;
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openId      用户的唯一标识
     * @param lang        返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语
     * @return Oauth2UserInfo
     * @throws IOException IOException
     */
    public Oauth2UserInfo userInfo(String accessToken, String openId, String lang) throws IOException {
        //获得接口地址
        String path = weixinMpApiProperties.getSnsPath().get("sns_userinfo");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append("?access_token=").append(accessToken).append("&openid=").append(openId).append("&lang=").append(lang);
        //请求
        ResponseEntity<String> result = externalRestTemplate.getForEntity(url.toString(), String.class);
        //解析结果
        Oauth2UserInfo oauth2UserInfo = objectMapper.readValue(result.getBody(), Oauth2UserInfo.class);
        //验证
        WeixinMpConsont.checkResult(oauth2UserInfo);
        //返回
        return oauth2UserInfo;
    }

    /**
     * 检验授权凭证（access_token）是否有效
     *
     * @param accessToken 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     * @param openId      用户的唯一标识
     * @return BaseResult
     * @throws IOException IOException
     */
    public BaseResult auth(String accessToken, String openId) throws IOException {
        //获得接口地址
        String path = weixinMpApiProperties.getSnsPath().get("sns_auth");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append("?access_token=").append(accessToken).append("&openid=").append(openId);
        //请求
        ResponseEntity<String> result = externalRestTemplate.getForEntity(url.toString(), String.class);
        //解析结果
        BaseResult baseResult = objectMapper.readValue(result.getBody(), BaseResult.class);
        //验证
        WeixinMpConsont.checkResult(baseResult);
        //返回
        return baseResult;
    }

}
