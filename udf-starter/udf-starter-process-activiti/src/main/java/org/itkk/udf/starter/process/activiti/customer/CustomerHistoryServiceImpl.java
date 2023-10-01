package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.impl.HistoryServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class CustomerHistoryServiceImpl extends HistoryServiceImpl {
    public CustomerHistoryServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    public CustomerHistoricTaskInstanceQueryImpl createCustomerHistoricTaskInstanceQuery() {
        return new CustomerHistoricTaskInstanceQueryImpl(commandExecutor, processEngineConfiguration.getDatabaseType());
    }
}
