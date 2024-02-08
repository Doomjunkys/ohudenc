package org.itkk.udf.api.rbac.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Valid
public class OptionDto implements Serializable {

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
    private String name;

}
