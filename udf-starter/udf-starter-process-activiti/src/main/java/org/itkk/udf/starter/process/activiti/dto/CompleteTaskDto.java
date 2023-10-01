package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class CompleteTaskDto {
    /**
     * 流程实例ID
     */
    @NotBlank(message = "任务ID不能为空")
    private String processInstanceId;

    /**
     * 任务ID
     */
    @NotBlank(message = "任务ID不能为空")
    private String taskId;

    /**
     * 备注类型 (1：通过 , -1：驳回)
     */
    @NotBlank(message = "备注类型不能为空")
    private String commentType;

    /**
     * 备注描述
     */
    private String commentMessage;

    /**
     * 流程参数
     */
    @NotNull(message = "流程参数不能为空")
    private Map<String, Object> variables;
}
