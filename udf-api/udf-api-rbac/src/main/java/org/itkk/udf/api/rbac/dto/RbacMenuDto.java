package org.itkk.udf.api.rbac.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.itkk.udf.api.rbac.RbacConstant;

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
public class RbacMenuDto implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = -4436379076719077791L;

    /**
     * 名称
     */
    @NotBlank(message = "menu的name不能为空")
    private String name;

    /**
     * 样式名称
     */
    @NotBlank(message = "menu的className不能为空")
    private String className;

    /**
     * 父节点
     */
    @NotBlank(message = "menu的parentCode不能为空")
    private String parentCode = RbacConstant.ROOT_MENU_CODE;

    /**
     * 排序
     */
    @NotNull(message = "menu的order不能为空")
    private Integer order = 1;

    /**
     * 功能
     */
    private List<String> functions;

}
