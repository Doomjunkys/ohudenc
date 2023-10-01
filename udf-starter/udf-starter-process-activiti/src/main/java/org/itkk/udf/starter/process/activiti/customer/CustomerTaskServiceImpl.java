package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.impl.TaskServiceImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;

public class CustomerTaskServiceImpl extends TaskServiceImpl {
    public CustomerTaskServiceImpl(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    public CustomerTaskQueryImpl createCustomerTaskQuery() {
        return new CustomerTaskQueryImpl(commandExecutor, processEngineConfiguration.getDatabaseType());
    }
}
