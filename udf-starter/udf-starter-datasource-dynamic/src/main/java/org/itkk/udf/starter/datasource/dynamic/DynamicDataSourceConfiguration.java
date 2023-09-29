/**
 * DruidDataSourceConfig.java
 * Created at 2016-11-02
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package org.itkk.udf.starter.datasource.dynamic;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * 描述 : DynamicDataSourceConfig
 *
 * @author wangkang
 */
@Configuration
public class DynamicDataSourceConfiguration {

    /**
     * 构造数据源
     *
     * @return DataSource
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(new HashMap<>());
        return dynamicDataSource;
    }

}
