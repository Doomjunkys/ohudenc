package org.itkk.udf.api.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

/**
 * 属性类
 */
@Component
@ConfigurationProperties(prefix = CommonProperties.UDF_API_PROPERTIES)
@Validated
@Data
public class CommonProperties {
    /**
     * 配置文件前缀
     */
    protected static final String UDF_API_PROPERTIES = "org.itkk.udf.api.common.properties";
}
