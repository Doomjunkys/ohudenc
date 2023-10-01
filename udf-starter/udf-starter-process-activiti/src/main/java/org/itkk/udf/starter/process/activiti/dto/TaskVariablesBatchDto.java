package org.itkk.udf.starter.process.activiti.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class TaskVariablesBatchDto {
    /**
     * 任务ID
     */
    @NotNull(message = "任务ID列表不能为空")
    private List<String> taskIdList;

    /**
     * 流程参数
     */
    @NotNull(message = "流程参数不能为空")
    private List<String> variableNames;
}
