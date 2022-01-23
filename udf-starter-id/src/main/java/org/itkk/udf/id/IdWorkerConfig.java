package org.itkk.udf.id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IdWorkerConfig
 */
@Configuration
public class IdWorkerConfig {

    /**
     * idWorkerProperties
     */
    @Autowired
    private IdWorkerProperties idWorkerProperties;

    /**
     * 描述 : 分布式ID生成器
     *
     * @return 分布式ID生成器
     */
    @Bean
    public IdWorker idWorker() {
        return new IdWorker(idWorkerProperties.getType());
    }

}
