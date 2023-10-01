package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.data.impl.MybatisTaskDataManager;
import org.activiti.engine.task.Task;

import java.util.Collections;
import java.util.List;

public class CustomerMybatisTaskDataManager extends MybatisTaskDataManager {
    public CustomerMybatisTaskDataManager(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    public long customerFindTaskCountByQueryCriteria(CustomerTaskQueryImpl taskQuery) {
        return (Long) getDbSqlSession().selectOne("customerSelectTaskCountByQueryCriteria", taskQuery);
    }

    public List<Task> customerFindTasksByQueryCriteria(CustomerTaskQueryImpl taskQuery) {
        final String query = "customerSelectTaskByQueryCriteria";
        return getDbSqlSession().selectList(query, taskQuery);
    }

    public List<Task> customerFindTasksAndVariablesByQueryCriteria(CustomerTaskQueryImpl taskQuery) {
        final String query = "customerSelectTaskWithVariablesByQueryCriteria";
        // paging doesn't work for combining task instances and variables due to
        // an outer join, so doing it in-memory
        if (taskQuery.getFirstResult() < 0 || taskQuery.getMaxResults() <= 0) {
            return Collections.EMPTY_LIST;
        }

        int firstResult = taskQuery.getFirstResult();
        int maxResults = taskQuery.getMaxResults();

        // setting max results, limit to 20000 results for performance reasons
        if (taskQuery.getTaskVariablesLimit() != null) {
            taskQuery.setMaxResults(taskQuery.getTaskVariablesLimit());
        } else {
            taskQuery.setMaxResults(getProcessEngineConfiguration().getTaskQueryLimit());
        }
        taskQuery.setFirstResult(0);

        List<Task> instanceList = getDbSqlSession().selectListWithRawParameterWithoutFilter(query, taskQuery, taskQuery.getFirstResult(), taskQuery.getMaxResults());

        if (instanceList != null && !instanceList.isEmpty()) {
            if (firstResult > 0) {
                if (firstResult <= instanceList.size()) {
                    int toIndex = firstResult + Math.min(maxResults, instanceList.size() - firstResult);
                    return instanceList.subList(firstResult, toIndex);
                } else {
                    return Collections.EMPTY_LIST;
                }
            } else {
                int toIndex = Math.min(maxResults, instanceList.size());
                return instanceList.subList(0, toIndex);
            }
        }
        return Collections.EMPTY_LIST;
    }
}
