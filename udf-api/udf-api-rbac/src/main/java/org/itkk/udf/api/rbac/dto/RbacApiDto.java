package org.itkk.udf.api.rbac.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class RbacApiDto implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = 7991190522771955901L;

    /**
     * 名称
     */
    @NotBlank(message = "api的name不能为空")
    private String name;

    /**
     * 地址
     */
    @NotBlank(message = "api的uri不能为空")
    private String uri;

    /**
     * HTTP方法
     */
    @NotBlank(message = "api的method不能为空")
    private String method;

}
