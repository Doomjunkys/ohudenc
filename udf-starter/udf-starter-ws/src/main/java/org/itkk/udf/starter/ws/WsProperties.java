package org.itkk.udf.starter.ws;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = WsProperties.WS_PROPERTIES_PRE_FIX)
@Validated
@Data
public class WsProperties {
    /**
     * 配置文件前缀
     */
    protected static final String WS_PROPERTIES_PRE_FIX = "com.imaxus.ws.properties";

    /**
     * ws根url
     */
    private String wsRootUrl;
}
