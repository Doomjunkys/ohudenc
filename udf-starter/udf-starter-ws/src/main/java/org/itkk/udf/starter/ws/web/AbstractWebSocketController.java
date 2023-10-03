package org.itkk.udf.starter.ws.web;

import org.itkk.udf.starter.core.RestResponse;
import org.itkk.udf.starter.ws.AbstractWebSocketServer;
import org.itkk.udf.starter.ws.WsConstant;
import org.itkk.udf.starter.ws.dto.WsSendDto;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

public abstract class AbstractWebSocketController {

    /**
     * abstractWebSocketServer
     */
    @Autowired(required = false)
    private AbstractWebSocketServer abstractWebSocketServer;

    /**
     * 发送消息
     *
     * @param wsSendDto wsSendDto
     * @return RestResponse<String>
     * @throws IOException
     */
    @PostMapping(WsConstant.SEND_URL)
    public RestResponse<String> send(@RequestBody WsSendDto wsSendDto) throws IOException {
        //判空
        if (abstractWebSocketServer != null && wsSendDto != null && CollectionUtils.isNotEmpty(wsSendDto.getUserIds()) && wsSendDto.getReceive() != null) {
            //发送消息
            abstractWebSocketServer.sendLocal(wsSendDto.getUserIds(), wsSendDto.getReceive());
        }
        //返回
        return RestResponse.success();
    }

}
