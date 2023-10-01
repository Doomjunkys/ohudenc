package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class DelProcessInstanceDto {
    /**
     * 流程实例ID
     */
    @NotBlank(message = "流程实例ID不能为空")
    private String processInstanceId;
    /**
     * 删除理由
     */
    private String deleteReason;
}
