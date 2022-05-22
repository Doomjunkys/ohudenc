package org.itkk.udf.id;

import lombok.Data;
import org.itkk.udf.id.web.IIdWorkerController;
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
@Data
public class IdWorkerProperties {

    /**
     * 最大个数
     */
    private int maxCount = IIdWorkerController.MAX_COUNT;

    /**
     * ID构造类型
     */
    @NotNull
    private String type = IdWorker.UUID_TYPE;
}
