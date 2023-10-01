package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntityManagerImpl;
import org.activiti.engine.impl.persistence.entity.data.HistoricTaskInstanceDataManager;

import java.util.Collections;
import java.util.List;

public class CustomerHistoricTaskInstanceEntityManagerImpl extends HistoricTaskInstanceEntityManagerImpl {
    public CustomerHistoricTaskInstanceEntityManagerImpl(ProcessEngineConfigurationImpl processEngineConfiguration, HistoricTaskInstanceDataManager historicTaskInstanceDataManager) {
        super(processEngineConfiguration, historicTaskInstanceDataManager);
    }

    public long customerFindHistoricTaskInstanceCountByQueryCriteria(CustomerHistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        if (getHistoryManager().isHistoryEnabled()) {
            return ((CustomerMybatisHistoricTaskInstanceDataManager) historicTaskInstanceDataManager).customerFindHistoricTaskInstanceCountByQueryCriteria(historicTaskInstanceQuery);
        }
        return 0;
    }

    public List<HistoricTaskInstance> customerFindHistoricTaskInstancesByQueryCriteria(CustomerHistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        if (getHistoryManager().isHistoryEnabled()) {
            return ((CustomerMybatisHistoricTaskInstanceDataManager) historicTaskInstanceDataManager).customerFindHistoricTaskInstancesByQueryCriteria(historicTaskInstanceQuery);
        }
        return Collections.EMPTY_LIST;
    }

    public List<HistoricTaskInstance> customerFindHistoricTaskInstancesAndVariablesByQueryCriteria(CustomerHistoricTaskInstanceQueryImpl historicTaskInstanceQuery) {
        if (getHistoryManager().isHistoryEnabled()) {
            return ((CustomerMybatisHistoricTaskInstanceDataManager) historicTaskInstanceDataManager).customerFindHistoricTaskInstancesAndVariablesByQueryCriteria(historicTaskInstanceQuery);
        }
        return Collections.EMPTY_LIST;
    }
}
