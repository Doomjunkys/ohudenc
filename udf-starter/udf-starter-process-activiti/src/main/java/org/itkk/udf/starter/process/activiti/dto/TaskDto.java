package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.activiti.engine.task.DelegationState;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class TaskDto implements Serializable {
    private static final long serialVersionUID = -4559054134244971233L; //ID
    private String id; //任务的DB id
    private String name; //任务的名称或标题
    private String owner; //负责此任务的人员
    private String parentTaskId; //父任务ID
    private Integer priority; //指出这项任务的重要性/紧迫性
    private DelegationState delegationState; //任务委派状态
    private String assignee; //委派此任务的人员的
    private String category; //任务的类别
    private Date claimTime; //任务的认领时间
    private Date createTime; //创建此任务的日期/时间
    private String description; //任务的自由文本描述
    private Date dueDate; //任务截止日期
    private String executionId; //引用执行路径，如果与流程实例无关，则为null
    private String formKey; //用户任务的表单键
    private String processDefinitionId; //引用流程定义ID
    private String processInstanceId; //引用流程实例ID，如果与流程实例无关，则为null。
    private Map<String, Object> processVariables; //流程变量
    private String taskDefinitionKey; //定义此任务的进程中活动的id，如果与进程无关，则为null
    private Map<String, Object> taskLocalVariables; //任务本地变量
    private String tenantId; //此任务的租户标识符
    private Boolean isSuspended; //指示此任务是否已暂停
    private String processDefinitionKey;//流程定义键
    private String processDefinitionName; //流程定义名称
    private Date processInstanceStartTime; //流程开始时间
    private String startUserId; //流程发起人ID
    private String startUserName; //流程发起人名称
    private Long startUserDeptId; //流程发起人部门ID
    private String startUserDeptName; //流程发起人部门名称
}
