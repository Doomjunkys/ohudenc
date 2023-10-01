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
public class ProcessedListDto {
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
     * 流程定义Key列表
     */
    private List<String> processDefinitionKeyList;

    /**
     * 是否包含流程变量
     */
    private Boolean includeProcessVariables;
}
