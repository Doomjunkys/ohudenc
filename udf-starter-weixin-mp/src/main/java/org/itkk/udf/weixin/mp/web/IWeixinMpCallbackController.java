package org.itkk.udf.weixin.mp.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * IWeixinMpCallbackController
 */
@ApiIgnore
public interface IWeixinMpCallbackController {

    /**
     * 微信验证(GET)
     *
     * @param businessCode 业务代码
     * @param signature    微信加密签名
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param echostr      随机数
     */
    @GetMapping
    void check(@PathVariable String businessCode, String signature, String timestamp, String nonce, String echostr);

    /**
     * 微信消息推送(POST)
     *
     * @param businessCode 业务代码
     * @param signature    微信加密签名
     * @param timestamp    时间戳
     * @param nonce        随机数
     * @param echostr      随机数
     */
    @PostMapping
    void callback(@PathVariable String businessCode, String signature, String timestamp, String nonce, String echostr);

}
