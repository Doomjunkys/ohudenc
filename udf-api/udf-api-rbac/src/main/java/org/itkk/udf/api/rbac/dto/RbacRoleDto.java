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
import java.util.Map;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class RbacRoleDto implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = -4436379076719077791L;

    /**
     * 名称
     */
    @NotBlank(message = "role.public的name不能为空")
    private String name;

    /**
     * 功能 , 操作
     */
    private Map<String, List<String>> function;

    /**
     * api
     */
    private List<String> apis;

}
