package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.HistoricTaskInstanceQueryImpl;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.data.impl.MybatisHistoricTaskInstanceDataManager;

import java.util.Collections;
import java.util.List;

public class CustomerMybatisHistoricTaskInstanceDataManager extends MybatisHistoricTaskInstanceDataManager {
    public CustomerMybatisHistoricTaskInstanceDataManager(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super(processEngineConfiguration);
    }

    public long customerFindHistoricTaskInstanceCountByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        return (Long) getDbSqlSession().selectOne("customerSelectHistoricTaskInstanceCountByQueryCriteria", historicTaskInstanceQuery);
    }

    public List<HistoricTaskInstance> customerFindHistoricTaskInstancesByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        return getDbSqlSession().selectList("customerSelectHistoricTaskInstancesByQueryCriteria", historicTaskInstanceQuery);
    }

    public List<HistoricTaskInstance> customerFindHistoricTaskInstancesAndVariablesByQueryCriteria(HistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        // paging doesn't work for combining task instances and variables
        // due to an outer join, so doing it in-memory
        if (historicTaskInstanceQuery.getFirstResult() < 0 || historicTaskInstanceQuery.getMaxResults() <= 0) {
            return Collections.EMPTY_LIST;
        }

        int firstResult = historicTaskInstanceQuery.getFirstResult();
        int maxResults = historicTaskInstanceQuery.getMaxResults();

        // setting max results, limit to 20000 results for performance reasons
        if (historicTaskInstanceQuery.getTaskVariablesLimit() != null) {
            historicTaskInstanceQuery.setMaxResults(historicTaskInstanceQuery.getTaskVariablesLimit());
        } else {
            historicTaskInstanceQuery.setMaxResults(getProcessEngineConfiguration().getHistoricTaskQueryLimit());
        }
        historicTaskInstanceQuery.setFirstResult(0);

        List<HistoricTaskInstance> instanceList = getDbSqlSession().selectListWithRawParameterWithoutFilter("customerSelectHistoricTaskInstancesWithVariablesByQueryCriteria", historicTaskInstanceQuery,
                historicTaskInstanceQuery.getFirstResult(), historicTaskInstanceQuery.getMaxResults());

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

        return instanceList;
    }
}
