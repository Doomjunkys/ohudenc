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
public class StartProcessInstanceDto {
    /**
     * 流程定义键
     */
    @NotBlank(message = "流程定义键不能为空")
    private String processDefinitionKey;
    /**
     * 业务键
     */
    private String businessKey;
    /**
     * 流程实例名称
     */
    private String processInstanceName;
    /**
     * 流程参数
     */
    @NotNull(message = "流程参数不能为空")
    private Map<String, Object> variables;
}
