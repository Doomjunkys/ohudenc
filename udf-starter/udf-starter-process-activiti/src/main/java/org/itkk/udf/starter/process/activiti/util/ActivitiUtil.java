package org.itkk.udf.starter.process.activiti.util;

import org.itkk.udf.starter.process.activiti.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 工具类
 */
@Slf4j
public class ActivitiUtil {

    /**
     * 构造流程业务键
     *
     * @param prefix prefix
     * @param userId userId
     * @param ids    ids
     * @return String
     */
    public static String buildBusinessKey(String prefix, String userId, String... ids) {
        final String split = "_";
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        sb.append(split);
        sb.append(userId);
        if (ArrayUtils.isNotEmpty(ids)) {
            sb.append(split);
            sb.append(String.join(split, ids));
        }
        return sb.toString();
    }

    /**
     * 构造流程备注DTO
     *
     * @param taskDefinitionName taskDefinitionName
     * @param userId             userId
     * @param userName           userName
     * @param userDeptId         userDeptId
     * @param userDeptName       userDeptName
     * @param comment            comment
     * @return CommentDto
     */
    public static CommentDto buildDto(String taskDefinitionName, String userId, String userName, Long userDeptId, String userDeptName, Comment comment) {
        return new CommentDto()
                .setId(comment.getId())
                .setProcessInstanceId(comment.getProcessInstanceId())
                .setTaskId(comment.getTaskId())
                .setType(comment.getType())
                .setTime(comment.getTime())
                .setFullMessage(comment.getFullMessage())
                .setUserId(userId)
                .setUserName(userName)
                .setUserDeptId(userDeptId)
                .setUserDeptName(userDeptName)
                .setTaskDefinitionName(taskDefinitionName);
    }

    /**
     * 构造流程定义Dto
     *
     * @param processDefinition processDefinition
     * @return ProcessDefinitionDto
     */
    public static ProcessDefinitionDto buildDto(ProcessDefinition processDefinition, String startFormKey) {
        return new ProcessDefinitionDto()
                .setCategory(processDefinition.getCategory())
                .setDeploymentId(processDefinition.getDeploymentId())
                .setDescription(processDefinition.getDescription())
                .setId(processDefinition.getId())
                .setKey(processDefinition.getKey())
                .setName(processDefinition.getName())
                .setVersion(processDefinition.getVersion())
                .setIsSuspended(processDefinition.isSuspended())
                .setStartFormKey(startFormKey);
        //.setTenantId(processDefinition.getTenantId())
        //.setDiagramResourceName(processDefinition.getDiagramResourceName())
        //.setEngineVersion(processDefinition.getEngineVersion())
        //.setResourceName(processDefinition.getResourceName())
        //.setHasGraphicalNotation(processDefinition.hasGraphicalNotation())
        //.setHasStartFormKey(processDefinition.hasStartFormKey())
    }

    /**
     * 构造任务实例Dto
     *
     * @param processDefinitionKey     processDefinitionKey
     * @param processDefinitionName    processDefinitionName
     * @param processInstanceStartTime processInstanceStartTime
     * @param startUserId              startUserId
     * @param startUserName            startUserName
     * @param startUserDeptId          startUserDeptId
     * @param startUserDeptName        startUserDeptName
     * @param task                     task
     * @return TaskDto
     */
    public static TaskDto buildDto(String processDefinitionKey, String processDefinitionName, Date processInstanceStartTime, String startUserId, String startUserName, Long startUserDeptId, String startUserDeptName, Task task) {
        return new TaskDto()
                .setId(task.getId())
                .setName(task.getName())
                .setOwner(task.getOwner())
                .setParentTaskId(task.getParentTaskId())
                .setPriority(task.getPriority())
                .setDelegationState(task.getDelegationState())
                .setAssignee(task.getAssignee())
                .setCategory(task.getCategory())
                .setClaimTime(task.getClaimTime())
                .setCreateTime(task.getCreateTime())
                .setDescription(task.getDescription())
                .setDueDate(task.getDueDate())
                .setExecutionId(task.getExecutionId())
                .setFormKey(task.getFormKey())
                .setProcessDefinitionId(task.getProcessDefinitionId())
                .setProcessInstanceId(task.getProcessInstanceId())
                .setProcessVariables(task.getProcessVariables())
                .setTaskDefinitionKey(task.getTaskDefinitionKey())
                .setTaskLocalVariables(task.getTaskLocalVariables())
                .setTenantId(task.getTenantId())
                .setIsSuspended(task.isSuspended())
                .setProcessDefinitionKey(processDefinitionKey)
                .setProcessDefinitionName(processDefinitionName)
                .setProcessInstanceStartTime(processInstanceStartTime)
                .setStartUserId(startUserId)
                .setStartUserName(startUserName)
                .setStartUserDeptId(startUserDeptId)
                .setStartUserDeptName(startUserDeptName);
    }

    /**
     * 构造历史任务实例Dto
     *
     * @param historicTaskInstance historicTaskInstance
     * @return HistoricTaskInstanceDto
     */
    public static HistoricTaskInstanceDto buildDto(HistoricTaskInstance historicTaskInstance, String processDefinitionKey, String processDefinitionName, String userId, String userName, Long userDeptId, String userDeptName) {
        return new HistoricTaskInstanceDto()
                .setId(historicTaskInstance.getId())
                .setExecutionId(historicTaskInstance.getExecutionId())
                .setParentTaskId(historicTaskInstance.getParentTaskId())
                .setName(historicTaskInstance.getName())
                .setOwner(historicTaskInstance.getOwner())
                .setAssignee(historicTaskInstance.getAssignee())
                .setCreateTime(historicTaskInstance.getCreateTime())
                .setStartTime(historicTaskInstance.getStartTime())
                .setEndTime(historicTaskInstance.getEndTime())
                .setDueDate(historicTaskInstance.getDueDate())
                .setClaimTime(historicTaskInstance.getClaimTime())
                .setFormKey(historicTaskInstance.getFormKey())
                .setTime(historicTaskInstance.getTime())
                .setDeleteReason(historicTaskInstance.getDeleteReason())
                .setDurationInMillis(historicTaskInstance.getDurationInMillis())
                .setWorkTimeInMillis(historicTaskInstance.getWorkTimeInMillis())
                .setCategory(historicTaskInstance.getCategory())
                .setDescription(historicTaskInstance.getDescription())
                .setPriority(historicTaskInstance.getPriority())
                .setProcessInstanceId(historicTaskInstance.getProcessInstanceId())
                .setProcessVariables(historicTaskInstance.getProcessVariables())
                .setProcessDefinitionId(historicTaskInstance.getProcessDefinitionId())
                .setProcessDefinitionKey(processDefinitionKey)
                .setProcessDefinitionName(processDefinitionName)
                .setTaskDefinitionKey(historicTaskInstance.getTaskDefinitionKey())
                .setTaskLocalVariables(historicTaskInstance.getTaskLocalVariables())
                .setTenantId(historicTaskInstance.getTenantId())
                .setUserId(userId)
                .setUserName(userName)
                .setUserDeptId(userDeptId)
                .setUserDeptName(userDeptName);
    }

    /**
     * 构造历史流程实例DTO
     *
     * @param historicProcessInstance  historicProcessInstance
     * @param historicTaskInstanceDtos historicTaskInstanceDtos
     * @return HistoricProcessInstanceDto
     */
    public static HistoricProcessInstanceDto buildDto(HistoricProcessInstance historicProcessInstance, List<HistoricTaskInstanceDto> historicTaskInstanceDtos) {
        return new HistoricProcessInstanceDto()
                .setId(historicProcessInstance.getId())
                .setName(StringUtils.isNotBlank(historicProcessInstance.getName()) ? historicProcessInstance.getName() : historicProcessInstance.getProcessDefinitionName())
                .setBusinessKey(historicProcessInstance.getBusinessKey())
                .setDeleteReason(historicProcessInstance.getDeleteReason())
                .setDeploymentId(historicProcessInstance.getDeploymentId())
                .setDescription(historicProcessInstance.getDescription())
                .setProcessDefinitionId(historicProcessInstance.getProcessDefinitionId())
                .setProcessDefinitionKey(historicProcessInstance.getProcessDefinitionKey())
                .setProcessDefinitionName(historicProcessInstance.getProcessDefinitionName())
                .setProcessDefinitionVersion(historicProcessInstance.getProcessDefinitionVersion())
                .setProcessVariables(historicProcessInstance.getProcessVariables())
                .setStartTime(historicProcessInstance.getStartTime())
                .setEndTime(historicProcessInstance.getEndTime())
                .setStartUserId(historicProcessInstance.getStartUserId())
                .setDurationInMillis(historicProcessInstance.getDurationInMillis())
                .setStartActivityId(historicProcessInstance.getStartActivityId())
                .setEndActivityId(historicProcessInstance.getEndActivityId())
                .setSuperProcessInstanceId(historicProcessInstance.getSuperProcessInstanceId())
                .setTenantId(historicProcessInstance.getTenantId())
                .setHistoricTaskInstanceDtos(historicTaskInstanceDtos);
    }

    /**
     * 构造流程实例DTO
     *
     * @param processInstance
     * @return ProcessInstanceDto
     */
    public static ProcessInstanceDto buildDto(ProcessInstance processInstance) {
        return new ProcessInstanceDto()
                .setId(processInstance.getId())
                .setBusinessKey(StringUtils.isBlank(processInstance.getBusinessKey()) ? processInstance.getProcessInstanceId() : processInstance.getBusinessKey())
                .setName(processInstance.getName())
                .setProcessDefinitionId(processInstance.getProcessDefinitionId())
                .setProcessDefinitionKey(processInstance.getProcessDefinitionKey())
                .setStartTime(processInstance.getStartTime())
                .setDeploymentId(processInstance.getDeploymentId())
                .setDescription(processInstance.getDescription())
                .setLocalizedDescription(processInstance.getLocalizedDescription())
                .setLocalizedName(processInstance.getLocalizedName())
                .setProcessDefinitionName(processInstance.getProcessDefinitionName())
                .setProcessDefinitionVersion(processInstance.getProcessDefinitionVersion())
                .setProcessVariables(processInstance.getProcessVariables())
                .setStartUserId(processInstance.getStartUserId())
                .setTenantId(processInstance.getTenantId())
                .setIsSuspended(processInstance.isSuspended());
    }

    /**
     * 私有化构造函数
     */
    private ActivitiUtil() {

    }

}
