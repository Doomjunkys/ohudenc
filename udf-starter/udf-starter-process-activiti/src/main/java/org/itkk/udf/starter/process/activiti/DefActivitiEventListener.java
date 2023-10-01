package org.itkk.udf.starter.process.activiti;

import org.itkk.udf.starter.core.CoreConstant;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class DefActivitiEventListener implements ActivitiEventListener {
    @Override
    public void onEvent(ActivitiEvent event) {
        //判断流程定义ID 和 流程实例ID 不能为空
        if (StringUtils.isNotBlank(event.getProcessDefinitionId()) && StringUtils.isNotBlank(event.getProcessInstanceId())) {
            //获得必要bean
            ProcessEngine processEngine = CoreConstant.applicationContext.getBean(ProcessEngine.class);
            //获得流程定义
            ProcessDefinition processDefinition = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionId(event.getProcessDefinitionId()).singleResult();
            //判空
            if (processDefinition != null) {
                //获得流程定义键
                String processDefinitionKey = processDefinition.getKey();
                //判断bean是否存在
                if (CoreConstant.applicationContext.containsBean(processDefinitionKey)) {
                    //获得bean;
                    IDefActivitiEventListenerHandler iDefActivitiEventListenerHandler = CoreConstant.applicationContext.getBean(processDefinitionKey, IDefActivitiEventListenerHandler.class);
                    //判空
                    if (iDefActivitiEventListenerHandler != null) {
                        //调用
                        iDefActivitiEventListenerHandler.handle(event);
                    }
                }
            }
        }
    }

    @Override
    public boolean isFailOnException() {
        return false;
    }
}
