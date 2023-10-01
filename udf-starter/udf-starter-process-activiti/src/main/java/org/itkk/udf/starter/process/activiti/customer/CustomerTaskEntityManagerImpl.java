package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.TaskEntityManagerImpl;
import org.activiti.engine.impl.persistence.entity.data.TaskDataManager;
import org.activiti.engine.task.Task;

import java.util.List;

public class CustomerTaskEntityManagerImpl extends TaskEntityManagerImpl {
    public CustomerTaskEntityManagerImpl(ProcessEngineConfigurationImpl processEngineConfiguration, TaskDataManager taskDataManager) {
        super(processEngineConfiguration, taskDataManager);
    }

    public List<Task> customerFindTasksByQueryCriteria(CustomerTaskQueryImpl taskQuery) {
        return  ((CustomerMybatisTaskDataManager) taskDataManager).customerFindTasksByQueryCriteria(taskQuery);
    }

    public List<Task> customerFindTasksAndVariablesByQueryCriteria(CustomerTaskQueryImpl taskQuery) {
        return ((CustomerMybatisTaskDataManager) taskDataManager).customerFindTasksAndVariablesByQueryCriteria(taskQuery);
    }

    public long customerFindTaskCountByQueryCriteria(CustomerTaskQueryImpl taskQuery) {
        return ((CustomerMybatisTaskDataManager) taskDataManager).customerFindTaskCountByQueryCriteria(taskQuery);
    }
}
