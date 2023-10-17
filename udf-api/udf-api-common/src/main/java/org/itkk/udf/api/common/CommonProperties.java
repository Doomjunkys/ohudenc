package org.itkk.udf.api.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

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

    /**
     * 追踪日志保留天数
     */
    @NotNull(message = UDF_API_PROPERTIES + ".traceLogKeepDays不能为空")
    private Integer traceLogKeepDays = 5;

    /**
     * 发送邮件队列并发数
     */
    private Integer sendEmailQueueMaxConcurrency = 1;
}
