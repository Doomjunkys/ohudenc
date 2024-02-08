package org.itkk.udf.api.rbac.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class RbacFunctionOptionDto implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = -4436379076719077791L;

    /**
     * 名称
     */
    @NotBlank(message = "function.option的name不能为空")
    private String name;

    /**
     * API列表
     */
    private List<String> apis;

}
