package org.itkk.udf.id;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * IdWorkerProperties
 */
@Component
@ConfigurationProperties(prefix = "org.itkk.id.properties")
@Validated
public class IdWorkerProperties {

    /**
     * ID构造类型
     */
    @NotNull
    private String type = IdWorker.UUID_TYPE;

    /**
     * D构造类型
     *
     * @return String
     */
    public String getType() {
        return type;
    }

    /**
     * D构造类型
     *
     * @param type type
     */
    public void setType(String type) {
        this.type = type;
    }
}
