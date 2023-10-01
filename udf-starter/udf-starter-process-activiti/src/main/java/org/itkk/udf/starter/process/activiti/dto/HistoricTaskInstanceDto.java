package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class HistoricTaskInstanceDto implements Serializable {
    private static final long serialVersionUID = 1366223725148535755L; //ID
    private String id; //任务的DB id
    private String executionId; //引用执行路径，如果与流程实例无关，则为null
    private String parentTaskId; //父任务ID
    private String name; //任务的名称或标题
    private String owner; //负责此任务的人员
    private String assignee; //委派此任务的人员的
    private Date createTime; //创建此任务的日期/时间
    private Date startTime; //任务开始的时间
    private Date endTime; //任务被删除或完成的时间
    private Date dueDate; //任务截止日期
    private Date claimTime; //任务的认领时间
    private String formKey; //用户任务的表单键
    private Date time; //时间(?)
    private String deleteReason; //删除此任务的原因
    private Long durationInMillis; //任务耗时 (endTime-startTime)
    private Long workTimeInMillis; //处理耗时(endTime-claimTime)
    private String category; //任务的类别
    private String description; //任务的自由文本描述
    private Integer priority; //指出这项任务的重要性/紧迫性
    private String processInstanceId; //引用流程实例ID，如果与流程实例无关，则为null。
    private Map<String, Object> processVariables; //流程变量
    private String processDefinitionId; //引用流程定义ID
    private String processDefinitionKey; //此流程的流程定义键
    private String processDefinitionName; //此流程的流程定义名称
    private String taskDefinitionKey; //定义此任务的进程中活动的id，如果与进程无关，则为null
    private Map<String, Object> taskLocalVariables; //本地任务变量
    private String tenantId; //此任务的租户标识符
    private String userId; //流程发起人ID
    private String userName; //流程发起人名称
    private Long userDeptId; //流程发起人部门ID
    private String userDeptName; //流程发起人部门名称
}
