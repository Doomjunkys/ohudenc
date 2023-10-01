package org.itkk.udf.starter.process.activiti.customer;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.impl.Page;
import org.activiti.engine.impl.TaskQueryImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;

import java.util.List;

public class CustomerTaskQueryImpl extends TaskQueryImpl {
    private static final long serialVersionUID = -1049256427838756945L;
    protected List<String> keyNotInList;

    public CustomerTaskQueryImpl(CommandExecutor commandExecutor, String databaseType) {
        super(commandExecutor, databaseType);
    }

    public TaskQuery taskDefinitionKeyNotInList(List<String> keyNotInList) {
        if (keyNotInList == null) {
            throw new ActivitiIllegalArgumentException("keyNotInList is null");
        }
        if (keyNotInList.isEmpty()) {
            throw new ActivitiIllegalArgumentException("keyNotInList is empty");
        }
        for (String key : keyNotInList) {
            if (key == null) {
                throw new ActivitiIllegalArgumentException("None of the given keyNotIn can be null");
            }
        }
        if (orActive) {
            ((CustomerTaskQueryImpl) currentOrQueryObject).keyNotInList = keyNotInList;
        } else {
            this.keyNotInList = keyNotInList;
        }
        return this;
    }

    public List<String> getTaskDefinitionKeyNotInList() {
        return keyNotInList;
    }

    @Override
    public List<Task> executeList(CommandContext commandContext, Page page) {
        ensureVariablesInitialized();
        checkQueryOk();
        List<Task> tasks = null;
        if (includeTaskLocalVariables || includeProcessVariables) {
            tasks = ((CustomerTaskEntityManagerImpl) commandContext.getTaskEntityManager()).customerFindTasksAndVariablesByQueryCriteria(this);
        } else {
            tasks = ((CustomerTaskEntityManagerImpl) commandContext.getTaskEntityManager()).customerFindTasksByQueryCriteria(this);
        }

        if (tasks != null && Context.getProcessEngineConfiguration().getPerformanceSettings().isEnableLocalization()) {
            for (Task task : tasks) {
                localize(task);
            }
        }
        return tasks;
    }

    @Override
    public long executeCount(CommandContext commandContext) {
        ensureVariablesInitialized();
        checkQueryOk();
        return ((CustomerTaskEntityManagerImpl) commandContext.getTaskEntityManager()).customerFindTaskCountByQueryCriteria(this);
    }
}
