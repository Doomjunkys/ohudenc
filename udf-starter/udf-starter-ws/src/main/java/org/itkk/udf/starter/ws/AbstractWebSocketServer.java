package org.itkk.udf.starter.ws;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreProperties;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.RestResponse;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.itkk.udf.starter.core.exception.handle.ExceptionHandle;
import org.itkk.udf.starter.core.exception.handle.IExceptionAlert;
import org.itkk.udf.starter.core.registration.IServiceRegistration;
import org.itkk.udf.starter.core.registration.ServiceDto;
import org.itkk.udf.starter.ws.dto.WsSendDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractWebSocketServer implements IWebSocketServer {

    /**
     * 会话缓存 ( key:sessionId , value:SessionDto )
     */
    private static final Map<String, SessionDto> sessionCache = new ConcurrentHashMap<>();

    /**
     * 获得校验token并且返回用户ID
     *
     * @param token token
     * @return String
     */
    protected abstract String checkAndGetUserId(String token);

    /**
     * 客户端连接建立成功时调用
     *
     * @param session session
     * @param token   token
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(WsConstant.TOKEN_NAME) String token) {
        //获得用户ID
        final String userId = this.checkAndGetUserId(token);
        //设置参数
        session.getUserProperties().put(WsConstant.TOKEN_NAME, token);
        session.getUserProperties().put(WsConstant.USER_ID_NAME, userId);
        //放入会话缓存
        sessionCache.put(session.getId(), new SessionDto().setSession(session).setUserId(userId));
        //日志输出当前用户数
        log.info("当前节点websocket用户数:{}", sessionCache.size());
    }

    /**
     * 客户端连接关闭调用的方法
     *
     * @param session session
     */
    @OnClose
    public void onClose(Session session) {
        //移除会话缓存
        sessionCache.remove(session.getId());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param session session
     * @param message message
     */
    @OnMessage
    public void onMessage(Session session, String message) throws Exception {
        //判空
        if (StringUtils.isNotBlank(message)) {
            //获得JSON转换器
            final ObjectMapper internalObjectMapper = CoreConstant.applicationContext.getBean("internalObjectMapper", ObjectMapper.class);
            //解析send对象
            final WsBase wsBase = internalObjectMapper.readValue(message, WsBase.class);
            //路由判空
            if (StringUtils.isBlank(wsBase.getRouting())) {
                throw new ParameterValidException("路由不能为空");
            }
            //解析路由
            final String[] routingArr = WsUtil.paseRouting(wsBase.getRouting());
            final String beanName = routingArr[0];
            final String methodName = routingArr[1];
            //获得处理器
            final Object handle = CoreConstant.applicationContext.getBean(beanName);
            //处理器判空
            if (handle == null) {
                throw new ParameterValidException("处理器" + beanName + "不存在");
            }
            //获得方法
            final Method method = handle.getClass().getMethod(methodName, WsSend.class);
            //获得参数类型
            JavaType sendType = internalObjectMapper.constructType(method.getParameterTypes()[0]);
            //解析参数
            WsSend<?> send = internalObjectMapper.readValue(message, sendType);
            //消息ID判空和初始化
            if (StringUtils.isBlank(send.getMessageId())) {
                send.setMessageId(UUID.randomUUID().toString());
            }
            //设置属性
            send.setMessageId(StringUtils.isBlank(send.getMessageId()) ? UUID.randomUUID().toString() : send.getMessageId());
            send.setUserId(session.getUserProperties().get(WsConstant.USER_ID_NAME).toString());
            send.setToken(session.getUserProperties().get(WsConstant.TOKEN_NAME).toString());
            //调用方法
            Object receiveObj = method.invoke(handle, send);
            //判空响应
            if (receiveObj != null) {
                //类型判断
                if (receiveObj instanceof WsReceive) {
                    //类型转换
                    WsReceive<?> receive = (WsReceive<?>) receiveObj;
                    //设置属性
                    receive.setMessageId(send.getMessageId());
                    receive.setRouting(send.getRouting());
                    //回复
                    this.send(session, receive);
                }
            }
        }
    }

    /**
     * 发生错误
     *
     * @param session session
     * @param e       e
     */
    @OnError
    public void onError(Session session, Throwable e) throws IOException {
        //获得配置属性bean
        final CoreProperties coreProperties = CoreConstant.applicationContext.getBean(CoreProperties.class);
        //处理
        try {
            //判断排除条件
            if (StringUtils.isNotBlank(coreProperties.getExceptionDetailLogExclude()) && coreProperties.getExceptionDetailLogExclude().indexOf(e.getClass().getName()) != -1) {
                //日志输出(警告)
                log.warn("warn log : {}", e.getMessage());
            } else {
                //获得异常通知bean
                IExceptionAlert iExceptionAlert = CoreConstant.applicationContext.getBean(IExceptionAlert.class);
                //异常通知
                if (iExceptionAlert != null) {
                    iExceptionAlert.alert(CoreUtil.getTraceId(), (Exception) e);
                }
                //日志输出(错误)
                log.error("error log : ", e);
            }
        } finally {
            //构造错误消息
            WsReceive<String> wsReceive = new WsReceive<String>()
                    .setCode(WsConstant.WS_RESPONSE_STATUS.FIAL.value())
                    .setResult(CoreConstant.FAIL_MSG)
                    .setErrorDetail(ExceptionHandle.buildErrorDetail(e));
            //补充属性
            wsReceive.setMessageId(UUID.randomUUID().toString());
            wsReceive.setRouting(WsConstant.DEF_SERVER_PUSH_ROUTING_NAME);
            //如果关闭了异常堆栈输出,则将异常堆栈设置为空
            if (!coreProperties.isOutputExceptionStackTrace()) {
                wsReceive.getErrorDetail().setStackTrace(null);
            }
            //发送错误消息
            this.send(session, wsReceive);
        }
    }

    @Override
    public <T> void send(String token, String userId, WsReceive<T> receive) {
        this.send(token, Lists.newArrayList(userId), receive);
    }

    @Override
    public <T> void send(String token, List<String> userIds, WsReceive<T> receive) {
        //获得必要bean
        final IServiceRegistration iServiceRegistration = CoreConstant.applicationContext.getBean(IServiceRegistration.class);
        final RestTemplate restTemplate = CoreConstant.applicationContext.getBean(RestTemplate.class);
        final WsProperties wsProperties = CoreConstant.applicationContext.getBean(WsProperties.class);
        //处理
        try {
            //补充参数
            if (StringUtils.isBlank(receive.getMessageId())) {
                receive.setMessageId(UUID.randomUUID().toString());
            }
            if (StringUtils.isBlank(receive.getRouting())) {
                receive.setRouting(WsConstant.DEF_SERVER_PUSH_ROUTING_NAME);
            }
            //判空
            if (iServiceRegistration != null && StringUtils.isNotBlank(wsProperties.getWsRootUrl())) {
                //获得服务清单
                final List<ServiceDto> serviceDtoList = iServiceRegistration.list(iServiceRegistration.loacl().getApplicationName());
                //构造异步任务
                CompletableFuture<RestResponse<String>>[] futureArr = new CompletableFuture[serviceDtoList.size()];
                for (int i = 0; i < futureArr.length; i++) {
                    //获得服务
                    final ServiceDto remote = serviceDtoList.get(i);
                    //添加任务
                    futureArr[i] = CompletableFuture.supplyAsync(() -> {
                        //定义返回值
                        final RestResponse<String> rv;
                        //处理
                        try {
                            //判断是否本地
                            if (iServiceRegistration.isLocal(remote)) {
                                //本地发送
                                this.sendLocal(userIds, receive);
                                //设置返回值
                                rv = RestResponse.success();
                            } else {
                                //拼接url
                                final String url = "http://" + remote.getIp() + ":" + remote.getPort() + wsProperties.getWsRootUrl() + WsConstant.SEND_URL;
                                //构造参数
                                HttpHeaders headers = new HttpHeaders();
                                headers.setContentType(MediaType.APPLICATION_JSON);
                                headers.add(WsConstant.TOKEN_NAME, token);
                                WsSendDto wsSendDto = new WsSendDto().setUserIds(userIds).setReceive((WsReceive<Object>) receive);
                                //请求 && 返回
                                rv = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(wsSendDto, headers), new ParameterizedTypeReference<RestResponse<String>>() {
                                }).getBody();
                            }
                        } catch (Exception e) {
                            throw new SystemRuntimeException(e);
                        }
                        //返回
                        return rv;
                    });
                    //执行任务
                    CompletableFuture.allOf(futureArr).get();
                }
            } else {
                //本地发送
                this.sendLocal(userIds, receive);
            }
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 发送消息
     *
     * @param userIds userIds
     * @param receive receive
     * @throws IOException
     */
    public void sendLocal(List<String> userIds, WsReceive<?> receive) throws IOException {
        //判空
        if (CollectionUtils.isNotEmpty(userIds)) {
            //遍历
            for (String userId : userIds) {
                //寻找session
                List<Session> sessionList = sessionCache.values().stream().filter(a -> a.getUserId().equals(userId)).map(SessionDto::getSession).collect(Collectors.toList());
                //判空
                if (CollectionUtils.isNotEmpty(sessionList)) {
                    //遍历
                    for (Session session : sessionList) {
                        this.send(session, receive);
                    }
                }
            }
        }
    }

    /**
     * 发送消息
     *
     * @param session session
     * @param receive receive
     * @throws IOException
     */
    private void send(Session session, Object receive) throws IOException {
        //获得配置属性bean
        final ObjectMapper internalObjectMapper = CoreConstant.applicationContext.getBean("internalObjectMapper", ObjectMapper.class);
        //判空
        if (receive != null) {
            //解析响应
            String receiveTxt = internalObjectMapper.writeValueAsString(receive);
            //判断链接是否打开
            if (session.isOpen()) {
                //发送
                session.getBasicRemote().sendText(receiveTxt);
            }
        }
    }

    /**
     * session缓存对象
     */
    @Data
    @Accessors(chain = true)
    @ToString
    @EqualsAndHashCode(callSuper = false)
    private static class SessionDto {
        private Session session; //会话
        private String userId; //用户ID
    }

}
