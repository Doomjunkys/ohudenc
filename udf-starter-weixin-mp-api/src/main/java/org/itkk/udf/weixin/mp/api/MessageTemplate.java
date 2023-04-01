package org.itkk.udf.weixin.mp.api;

import org.itkk.udf.weixin.mp.api.domain.SendMessageTemplateResult;
import org.itkk.udf.weixin.mp.api.domain.SendMessageTemplateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * MessageTemplate
 */
@Component
public class MessageTemplate {

    /**
     * weixinMpApiProperties
     */
    @Autowired
    private WeixinMpApiProperties weixinMpApiProperties;

    /**
     * token
     */
    @Autowired
    private Token token;

    /**
     * externalRestTemplate
     */
    @Autowired
    @Qualifier("externalRestTemplate")
    private RestTemplate externalRestTemplate;

    /**
     * SendMessageTemplateResult
     *
     * @param businessCode businessCode
     * @param param        param
     * @return SendMessageTemplateResult
     */
    public SendMessageTemplateResult send(String businessCode, SendMessageTemplateVo param) {
        //获得接口地址
        String path = weixinMpApiProperties.getApiPath().get("message_template_send");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append(WeixinMpConsont.appendAccessToken(token.get(businessCode)));
        //请求
        SendMessageTemplateResult result = externalRestTemplate.postForEntity(url.toString(), param, SendMessageTemplateResult.class).getBody();
        //验证
        WeixinMpConsont.checkResult(result);
        //返回
        return result;
    }

}
