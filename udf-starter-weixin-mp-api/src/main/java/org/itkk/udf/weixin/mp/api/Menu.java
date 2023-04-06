package org.itkk.udf.weixin.mp.api;

import org.itkk.udf.core.domain.weixin.mp.BaseResult;
import org.itkk.udf.weixin.mp.api.domain.MenuGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * Menu
 */
@Component
public class Menu {

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
     * 创建菜单
     *
     * @param businessCode 业务代码
     * @param menuGroup    菜单组
     * @return BaseResult
     */
    public BaseResult create(String businessCode, MenuGroup menuGroup) {
        //获得接口地址
        String path = weixinMpApiProperties.getApiPath().get("menu_create");
        //拼接url
        StringBuilder url = new StringBuilder(path);
        url.append(WeixinMpConsont.appendAccessToken(token.get(businessCode)));
        //请求
        BaseResult result = externalRestTemplate.postForEntity(url.toString(), menuGroup, BaseResult.class).getBody();
        //验证
        WeixinMpConsont.checkResult(result);
        //返回
        return result;
    }

}
