package org.itkk.udf.starter.process.activiti;

import org.itkk.udf.starter.process.activiti.customer.*;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.SecurityAutoConfiguration;
import org.apache.commons.collections4.SetUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Collections;

@Configuration
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class ActivitiConfiguration extends AbstractProcessEngineAutoConfiguration {

    /**
     * 配置
     */
    @Autowired
    private ActivitiProperties activitiProperties;

    @Bean
    public SpringProcessEngineConfiguration springProcessEngineConfiguration(DataSource dataSource, PlatformTransactionManager transactionManager, SpringAsyncExecutor springAsyncExecutor) throws IOException {
        //默认构造
        SpringProcessEngineConfiguration configuration = this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        // 是否自动创建流程引擎表
        configuration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        configuration.setAsyncExecutorActivate(false);
        // 流程历史等级
        configuration.setHistoryLevel(activitiProperties.getHistoryLevel());
        // 流程图字体
        configuration.setActivityFontName(activitiProperties.getFontName());
        configuration.setAnnotationFontName(activitiProperties.getFontName());
        configuration.setLabelFontName(activitiProperties.getFontName());
        //监听器
        configuration.setEventListeners(Collections.singletonList(new DefActivitiEventListener()));
        //customer HistoryService
        configuration.setHistoryService(new CustomerHistoryServiceImpl(configuration));
        configuration.setHistoricTaskInstanceDataManager(new CustomerMybatisHistoricTaskInstanceDataManager(configuration));
        configuration.setHistoricTaskInstanceEntityManager(new CustomerHistoricTaskInstanceEntityManagerImpl(configuration, configuration.getHistoricTaskInstanceDataManager()));
        //customer TaskService
        configuration.setTaskService(new CustomerTaskServiceImpl(configuration));
        configuration.setTaskDataManager(new CustomerMybatisTaskDataManager(configuration));
        configuration.setTaskEntityManager(new CustomerTaskEntityManagerImpl(configuration, configuration.getTaskDataManager()));
        //customer Mapper
        configuration.setCustomMybatisXMLMappers(SetUtils.hashSet("customer/CustomerTask.xml", "customer/CustomerHistoricTaskInstance.xml"));
        //返回
        return configuration;
    }

}
