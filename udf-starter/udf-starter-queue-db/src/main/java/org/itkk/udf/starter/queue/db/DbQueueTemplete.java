package org.itkk.udf.starter.queue.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class DbQueueTemplete {

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    /**
     * dbQueueProperties
     */
    @Autowired
    private DbQueueProperties dbQueueProperties;

    /**
     * 发送消息
     *
     * @param queueName      队列名称
     * @param hanledBeanName 处理器bean名称
     * @param body           消息体
     * @return 消息ID
     */
    public String send(String queueName, String hanledBeanName, Object body) {
        try {
            return send(queueName, hanledBeanName, internalObjectMapper.writeValueAsString(body));
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 发送主题消息
     *
     * @param topicName 主题名称
     * @param body      消息体
     * @return List<String>
     */
    public List<String> sendTopic(String topicName, Object body) {
        try {
            return sendTopic(topicName, internalObjectMapper.writeValueAsString(body));
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 发送主题消息
     *
     * @param topicName 主题名称
     * @param body      消息体
     * @return List<String>
     */
    public List<String> sendTopic(String topicName, String body) {
        //校验
        if (MapUtils.isEmpty(dbQueueProperties.getTopic())) {
            throw new ParameterValidException("消息主题未定义");
        }
        if (MapUtils.isEmpty(dbQueueProperties.getTopic().get(topicName))) {
            throw new ParameterValidException("消息主题[" + topicName + "]不存在");
        }
        //定义返回值
        final List<String> rv = new ArrayList<>();
        //遍历
        dbQueueProperties.getTopic().get(topicName).forEach((queueName, hanledBeanName) -> {
            try {
                rv.add(this.send(queueName, hanledBeanName, body));
            } catch (Exception e) {
                log.warn(CoreUtil.getTraceId() + " 主题消息[" + topicName + "]发送队列失败", e);
            }
        });
        //返回
        return rv;
    }

    /**
     * 发送消息
     *
     * @param queueName      队列名称
     * @param hanledBeanName 处理器bean名称
     * @param body           消息体
     * @return 消息ID
     */
    public String send(String queueName, String hanledBeanName, String body) {
        //校验
        if (!CoreConstant.applicationContext.containsBean(queueName)) {
            throw new ParameterValidException("队列[" + queueName + "]不存在");
        }
        if (!CoreConstant.applicationContext.containsBean(hanledBeanName)) {
            throw new ParameterValidException("处理器[" + hanledBeanName + "]不存在");
        }
        //构造消息
        DbQueueMessage dbQueueMessage = new DbQueueMessage();
        dbQueueMessage.setId(UUID.randomUUID().toString());
        dbQueueMessage.setHanledBeanName(hanledBeanName);
        dbQueueMessage.setBody(body);
        //获得队列
        IDbQueue iDbQueue = CoreConstant.applicationContext.getBean(queueName, IDbQueue.class);
        //发送 并且 返回messageId
        return iDbQueue.producer(dbQueueMessage);
    }
}
