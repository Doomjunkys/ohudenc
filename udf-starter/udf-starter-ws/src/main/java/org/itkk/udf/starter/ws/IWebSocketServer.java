package org.itkk.udf.starter.ws;

import java.util.List;

public interface IWebSocketServer {
    /**
     * 发送消息
     *
     * @param token   token
     * @param userId  userId
     * @param receive receive
     * @param <T>     T
     */
    <T> void send(String token, String userId, WsReceive<T> receive);

    /**
     * 发送消息
     *
     * @param token   token
     * @param userIds userIds
     * @param receive receive
     * @param <T>     T
     */
    <T> void send(String token, List<String> userIds, WsReceive<T> receive);
}
