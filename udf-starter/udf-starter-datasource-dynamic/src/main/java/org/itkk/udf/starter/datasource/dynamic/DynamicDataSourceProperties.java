package org.itkk.udf.starter.datasource.dynamic;

import org.itkk.udf.starter.core.datasource.DataSourceMeta;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = DynamicDataSourceProperties.DYNAMIC_DATASOURCE_PROPERTIES_PRE_FIX)
@Validated
@Data
public class DynamicDataSourceProperties {

    /**
     * 配置文件前缀
     */
    protected static final String DYNAMIC_DATASOURCE_PROPERTIES_PRE_FIX = "org.itkk.udf.starter.datasource.dynamic.properties";

    /**
     * 默认数据代码
     */
    @NotBlank(message = DYNAMIC_DATASOURCE_PROPERTIES_PRE_FIX + ".defaultCode不能为空")
    private String defaultCode;

    /**
     * 数据源配置
     */
    @NotNull(message = DYNAMIC_DATASOURCE_PROPERTIES_PRE_FIX + ".map不能为空")
    private Map<String, DataSourceMeta> map;

}
