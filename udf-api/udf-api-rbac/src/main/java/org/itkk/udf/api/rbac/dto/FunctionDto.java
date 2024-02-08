package org.itkk.udf.api.rbac.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class FunctionDto implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = -4436379076719077791L;

    /**
     * 代码
     */
    private String code;

    /**
     * 名称
     */
    @NotBlank(message = "function的name不能为空")
    private String name;

    /**
     * 排序
     */
    @NotNull(message = "function的order不能为空")
    private Integer order = 1;

    /**
     * 子操作
     */
    private List<OptionDto> options;

}
