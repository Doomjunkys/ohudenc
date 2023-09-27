package org.itkk.udf.starter.core.datasource;

import javax.sql.DataSource;

/**
 * 描述 : 构建数据源
 *
 * @author wangkang
 */
public interface IBuildDataSource {

    /**
     * 描述 : 构建数据源
     *
     * @param dataSourceMeta 数据源参数
     * @return 数据源
     */
    DataSource build(DataSourceMeta dataSourceMeta);

}
