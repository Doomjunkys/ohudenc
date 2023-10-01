package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class HistoricProcessInstanceDto implements Serializable {
    private static final long serialVersionUID = -2744210707445698835L; //ID
    private String id; //流程实例ID
    private String name; //流程实例的名称
    private String businessKey; //此流程实例的业务键
    private String deleteReason; //流程实例删除的原因
    private String deploymentId; //流程实例的流程定义的部署ID
    private String description; //流程实例的描述
    private String processDefinitionId; //此流程的流程定义ID
    private String processDefinitionKey; //此流程的流程定义键
    private String processDefinitionName; //此流程的流程定义名称
    private Integer processDefinitionVersion; //此流程的流程版本号
    private Map<String, Object> processVariables; //流程变量
    private Date startTime; //流程开始的时间
    private Date endTime; //流程结束的时间
    private String startUserId; //已启动此流程实例的经过身份验证的用户(流程发起人)
    private Long durationInMillis; //流程耗时(endTime - startTime)
    private String startActivityId; //开始活动的ID
    private String endActivityId; //结束活动的ID(请注意，流程实例可以有多个结束事件，在这种情况下，可能不确定哪个活动ID)
    private String superProcessInstanceId; //他处理潜在父流程实例的实例ID，如果不存在超级流程实例则为null
    private String tenantId; //流程实例的租户标识符
    private List<HistoricTaskInstanceDto> historicTaskInstanceDtos;//当前当前任务(只有进行中的流程有数据)
}
