package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.HistoricTaskInstanceQueryImpl;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;

import java.util.List;

public class CustomerHistoricTaskInstanceQueryImpl extends HistoricTaskInstanceQueryImpl {
    private static final long serialVersionUID = -4265819087524783999L;

    public CustomerHistoricTaskInstanceQueryImpl(CommandExecutor commandExecutor, String databaseType) {
        super(commandExecutor, databaseType);
    }

    @Override
    public List<HistoricTaskInstance> executeList(CommandContext commandContext, Page page) {
        ensureVariablesInitialized();
        checkQueryOk();
        List<HistoricTaskInstance> tasks = null;
        if (includeTaskLocalVariables || includeProcessVariables) {
            tasks = ((CustomerHistoricTaskInstanceEntityManagerImpl) commandContext.getHistoricTaskInstanceEntityManager()).customerFindHistoricTaskInstancesAndVariablesByQueryCriteria(this);
        } else {
            tasks = ((CustomerHistoricTaskInstanceEntityManagerImpl) commandContext.getHistoricTaskInstanceEntityManager()).customerFindHistoricTaskInstancesByQueryCriteria(this);
        }

        if (tasks != null && Context.getProcessEngineConfiguration().getPerformanceSettings().isEnableLocalization()) {
            for (HistoricTaskInstance task : tasks) {
                localize(task);
            }
        }

        return tasks;
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        ensureVariablesInitialized();
        checkQueryOk();
        return ((CustomerHistoricTaskInstanceEntityManagerImpl) commandContext.getHistoricTaskInstanceEntityManager()).customerFindHistoricTaskInstanceCountByQueryCriteria(this);
    }

}
