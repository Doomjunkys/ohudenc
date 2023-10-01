package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class ProcessDefinitionDto implements Serializable {
    private static final long serialVersionUID = 4873895194210499330L; //ID
    private String category; //类别名称
    private String deploymentId; //此流程定义的部署ID
    private String description; //描述
    private String id; //唯一标识符
    private String key; //此流程定义的所有版本的唯一名称
    private String name; //用于显示目的的标签
    private String startFormKey; //流程起始表单
    private Integer version; //此流程定义的版本
    private Boolean isSuspended; //如果流程定义处于挂起状态，则返回true。
    //private String tenantId; //此流程定义的租户标识符
    //private String diagramResourceName; //图表图像部署中的资源名称（如果有）
    //private String engineVersion; //此流程定义的引擎版本（5或6）
    //private String resourceName; //此流程定义的资源
    //private Boolean hasGraphicalNotation; //此流程定义是否已定义图形表示法
    //private Boolean hasStartFormKey; //此流程定义是否具有startFormKey
}
