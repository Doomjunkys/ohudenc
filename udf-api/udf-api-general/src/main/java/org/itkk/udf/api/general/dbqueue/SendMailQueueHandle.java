package org.itkk.udf.api.general.dbqueue;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.starter.core.exception.handle.IExceptionAlert;
import org.itkk.udf.starter.queue.db.IDbQueueHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component(CommonConstant.SEND_EMAIL_QUEUE_HANDLE)
@Slf4j
public class SendMailQueueHandle implements IDbQueueHandle {

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    /**
     * iExceptionAlert
     */
    @Autowired(required = false)
    private IExceptionAlert iExceptionAlert;

    @Override
    public void handle(String id, String body) {
        log.warn("邮件发送服务暂未实现");
    }
}
