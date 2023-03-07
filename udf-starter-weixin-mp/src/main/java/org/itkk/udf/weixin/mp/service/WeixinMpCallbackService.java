package org.itkk.udf.weixin.mp.service;

import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.amqp.rabbitmq.Rabbitmq;
import org.itkk.udf.amqp.rabbitmq.RabbitmqMessage;
import org.itkk.udf.weixin.mp.WeixinMpConsont;
import org.itkk.udf.weixin.mp.WeixinMpException;
import org.itkk.udf.weixin.mp.api.ShaOne;
import org.itkk.udf.weixin.mp.api.WeixinMpApiConfig;
import org.itkk.udf.weixin.mp.api.WeixinMpApiProperties;
import org.itkk.udf.weixin.mp.api.meta.WeixinMpApiAuthMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;

import java.util.*;

/**
 * WeixinMpCallbackService
 */
@Service
@Slf4j
public class WeixinMpCallbackService {

    /**
     * weixinMpApiProperties
     */
    @Autowired
    private WeixinMpApiProperties weixinMpApiProperties;

    /**
     * 描述 : rabbitmq
     */
    @Autowired
    private Rabbitmq rabbitmq;

    /**
     * check
     *
     * @param businessCode businessCode
     * @param signature    signature
     * @param timestamp    timestamp
     * @param nonce        nonce
     * @throws Exception Exception
     */
    public void check(String businessCode, String signature, String timestamp, String nonce) {
        final int num2 = 2;
        WeixinMpApiAuthMeta weixinMpApiAuthMeta = weixinMpApiProperties.getAuth().get(businessCode);
        List<String> params = new ArrayList<>();
        params.add(weixinMpApiAuthMeta.getToken());
        params.add(timestamp);
        params.add(nonce);
        //1. 将token、timestamp、nonce三个参数进行字典序排序
        Collections.sort(params, Comparator.naturalOrder());
        //2. 将三个参数字符串拼接成一个字符串进行sha1加密
        String temp = ShaOne.encode(params.get(0) + params.get(1) + params.get(num2));
        //3. 开发者获得加密后的字符串可与signature对比，如果不一致,则抛出异常
        if (!temp.equals(signature)) {
            throw new WeixinMpException("checkSignature Fail");
        }
    }

    /**
     * callback
     *
     * @param businessCode businessCode
     * @param signature    signature
     * @param timestamp    timestamp
     * @param nonce        nonce
     * @param inputMessage inputMessage
     * @return String
     * @throws Exception Exception
     */
    public String callback(String businessCode, String signature, String timestamp, String nonce, String inputMessage) throws Exception { //NOSONAR
        //常量
        final String defReturnMessage = "success";
        final String eventMessageType = "event";
        //获得身份认证元数据
        WeixinMpApiAuthMeta weixinMpApiAuthMeta = weixinMpApiProperties.getAuth().get(businessCode);
        //验证签名
        this.check(businessCode, signature, timestamp, nonce);
        //获得输入参数
        String inputXmlMessage = inputMessage;
        //判断是否需要解密
        if (weixinMpApiAuthMeta.getSafeModel()) {
            //实例化加密解密工具类
            WXBizMsgCrypt pc = new WXBizMsgCrypt(weixinMpApiAuthMeta.getToken(), weixinMpApiAuthMeta.getEncodingAesKey(), weixinMpApiAuthMeta.getAppId());
            //解析密文输入参数
            Element root = WeixinMpConsont.formatInputMessage(inputXmlMessage);
            String msgSignature = WeixinMpConsont.getXmlNodeValue(root, "MsgSignature");
            inputXmlMessage = pc.decryptMsg(msgSignature, timestamp, nonce, inputXmlMessage);
        }
        //解析明文输入参数
        Element root = WeixinMpConsont.formatInputMessage(inputXmlMessage);
        //获得消息类型
        String msgType = WeixinMpConsont.getXmlNodeValue(root, "MsgType");
        //定义MQ的交换机名称和路由键名称(默认是消息类型)
        String exchange = WeixinMpApiConfig.EXCHANGE_WEIXIN_MP_MESSAGE;
        String routingKey = msgType;
        //判断处理
        if (eventMessageType.equals(msgType)) { //事件
            //更改为事件类型
            exchange = WeixinMpApiConfig.EXCHANGE_WEIXIN_MP_EVENT;
            routingKey = WeixinMpConsont.getXmlNodeValue(root, "Event");
        }
        //构造消息头
        Map<String, String> header = new HashMap<>();
        header.put("businessCode", businessCode);
        //构造消息
        RabbitmqMessage<String> message = new RabbitmqMessage<>(header, inputXmlMessage);
        //发送消息
        rabbitmq.convertAndSend(exchange, routingKey, message);
        //日志输出
        log.info("send weixin message ---> businessCode : {} , exchange : {} , routingKey : {} , body : {} ", businessCode, exchange, routingKey, inputXmlMessage);
        //返回
        return defReturnMessage;
    }

}
