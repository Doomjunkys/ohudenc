package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class CommentDto implements Serializable {
    private static final long serialVersionUID = 322507996055909514L; //ID
    private String id; //此评论的唯一标识符
    private String processInstanceId; //引用此注释的流程实例
    private String taskId; //引用此评论的任务
    private String userId;//引用发表评论的用户
    private String type; //引用评论的类型
    private Date time; //用户发表评论的时间和日期
    private String fullMessage; //用户与任务和/或流程实例相关的完整注释消息
    private String userName; //流程发起人名称
    private Long userDeptId; //流程发起人部门ID
    private String userDeptName; //流程发起人部门名称
    private String taskDefinitionName;//任务定义名称
}
