package org.itkk.udf.api.common;

import org.itkk.udf.starter.cache.db.DbCacheServiceRegistration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置类
 */
@Configuration
public class CommonConfiguration implements WebMvcConfigurer {

    /**
     * dbCacheServiceRegistration
     *
     * @return DbCacheServiceRegistration
     */
    @Bean(destroyMethod = "shutdown")
    public DbCacheServiceRegistration dbCacheServiceRegistration() {
        return new DbCacheServiceRegistration();
    }

}
