package org.itkk.udf.id;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IdWorkerConfig
 */
@Configuration
public class IdWorkerConfig {


    /**
     * 分布式ID生成器
     *
     * @param idWorkerInit idWorkInit
     * @return idWorkInit
     */
    @Bean
    public IdWorker idWorker(IdWorkerInit idWorkerInit) {
        //初始化
        idWorkerInit.init();
        //返回
        return idWorkerInit.get();
    }

}
