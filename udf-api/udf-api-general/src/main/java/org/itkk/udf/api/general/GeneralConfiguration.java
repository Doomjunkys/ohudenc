package org.itkk.udf.api.general;


import org.itkk.udf.api.common.CommonConstant;
import org.itkk.udf.api.common.CommonProperties;
import org.itkk.udf.starter.queue.db.DbQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 配置类
 */
@Configuration
public class GeneralConfiguration {

    /**
     * commonProperties
     */
    @Autowired
    private CommonProperties commonProperties;

    /**
     * 发送邮件队列
     *
     * @return DbQueue
     */
    @Bean(name = CommonConstant.SEND_EMAIL_QUEUE, destroyMethod = "shutdown")
    public DbQueue sendEmailQueue() {
        return new DbQueue(CommonConstant.SEND_EMAIL_QUEUE, commonProperties.getSendEmailQueueMaxConcurrency());
    }

}
