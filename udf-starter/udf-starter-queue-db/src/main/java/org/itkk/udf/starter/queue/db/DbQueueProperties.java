package org.itkk.udf.starter.queue.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = DbQueueProperties.DB_QUEUE_PROPERTIES_PRE_FIX)
@Validated
@Data
public class DbQueueProperties {

    /**
     * 配置文件前缀
     */
    protected static final String DB_QUEUE_PROPERTIES_PRE_FIX = "com.imaxus.db.queue.properties";

    /**
     * TOPIC定义(topic名称.queue名称.hanledBean名称)
     */
    private Map<String, Map<String, String>> topic;

}
