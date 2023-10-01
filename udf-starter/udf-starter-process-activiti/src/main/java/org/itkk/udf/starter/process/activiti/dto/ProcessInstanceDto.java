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
public class ProcessInstanceDto implements Serializable {
    private static final long serialVersionUID = 8438196974729405269L; //ID
    private String id; //此流程实例ID (唯一标识符)
    private String businessKey; //此流程实例的业务键
    private String name; //此流程实例的名称
    private String processDefinitionId; //流程实例的流程定义的id
    private String processDefinitionKey; //流程实例的流程定义的键
    private Date startTime; //此流程实例的开始时间
    private String deploymentId; //流程实例的流程定义的部署ID
    private String description; //流程实例的描述
    private String localizedDescription; //流程实例的本地化描述
    private String localizedName; //此流程实例的本地化名称
    private String processDefinitionName; //流程实例的流程定义的名称
    private Integer processDefinitionVersion; //流程实例的流程定义的版本
    private Map<String, Object> processVariables; //流程变量
    private String startUserId; //此流程实例的用户标识(流程发起人)
    private String tenantId; //此流程实例的租户标识符
    private Boolean isSuspended; //如果流程实例被挂起，则返回true
}
