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
public class StartProcessInstanceListDto {
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
     * 是否结束
     */
    private Boolean finished;

    /**
     * 是否删除
     */
    private Boolean deleted;
}
