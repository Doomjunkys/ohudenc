package org.itkk.udf.starter.datasource.dynamic;

import org.itkk.udf.starter.core.CoreUtil;
import org.itkk.udf.starter.core.datasource.DataSourceMeta;
import org.itkk.udf.starter.core.datasource.IBuildDataSource;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源
 */
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    /**
     * 描述 : 目标数据库
     */
    private static Map<Object, Object> dyanmictargetDataSources = new ConcurrentHashMap<>();

    /**
     * 描述 : 动态数据源参数
     */
    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    /**
     * 描述 : 数据源构建
     */
    @Autowired
    private IBuildDataSource buildDataSource;

    @Override
    protected Object determineCurrentLookupKey() {
        //拿到code
        String dataSourceCode = DbContextHolder.getDataSourceCode();
        //判断code是否为空,如果为空,则设置为默认的数据源代码
        if (StringUtils.isBlank(dataSourceCode)) {
            dataSourceCode = dynamicDataSourceProperties.getDefaultCode();
            if (StringUtils.isBlank(dataSourceCode)) {
                throw new SystemRuntimeException("you do not specify a data source , current dataSourceCode is null");
            }
        }
        //拿到数据源属性,并且判断数据源属性是否存在,以及是否可用
        DataSourceMeta dataSourceProperties = dynamicDataSourceProperties.getMap().get(dataSourceCode);
        if (dataSourceProperties != null && dataSourceProperties.getEnable()) {
            Object obj = DynamicDataSource.dyanmictargetDataSources.get(dataSourceCode);
            if (obj == null) {
                DataSource dataSource = buildDataSource.build(dataSourceProperties);
                if (dataSource != null) {
                    DynamicDataSource.dyanmictargetDataSources.put(dataSourceCode, dataSource);
                    super.setTargetDataSources(DynamicDataSource.dyanmictargetDataSources);
                    super.afterPropertiesSet();
                }
            }
        } else {
            //如果在配置文件中,选择的数据源不存在,则也清理掉数据源缓存(可能是动态删除)
            if (DynamicDataSource.dyanmictargetDataSources.containsKey(dataSourceCode)) {
                DynamicDataSource.dyanmictargetDataSources.remove(dataSourceCode);
                super.setTargetDataSources(DynamicDataSource.dyanmictargetDataSources);
                super.afterPropertiesSet();
            }
            throw new SystemRuntimeException("dataSourceCode : " + dataSourceCode + " not exist or not enable");
        }
        if(log.isDebugEnabled()) {
            log.debug("{} switch to the Data Source : {}", CoreUtil.getTraceId(), dataSourceCode);
        }
        return dataSourceCode;
    }

}
