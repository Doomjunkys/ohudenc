package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class AllRuntimeTaskListDto {
    /**
     * 页数
     */
    @NotNull(message = "页数不能为空")
    private Integer page;
    /**
     * 行数
     */
    @NotNull(message = "行数不能为空")
    private Integer rows;

    /**
     * 流程定义名称
     */
    private String processDefinitionName;

    /**
     * 流程实例ID
     */
    private String processInstanceId;

    /**
     * 任务ID
     */
    private String taskId;
}
