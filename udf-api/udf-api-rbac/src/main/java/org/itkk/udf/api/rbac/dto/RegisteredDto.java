package org.itkk.udf.api.rbac.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class RegisteredDto implements Serializable {
    /**
     * ID
     */
    private static final long serialVersionUID = -6075064116058975444L;

    /**
     * 用户ID(UK_1)(非空)
     */
    @NotBlank(message = "用户名不能为空")
    @Length(max = 50, message = "用户名长度不能超过50个字符")
    private String userId;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Length(min = 6, message = "密码至少6个字符")
    private String pswd;

    /**
     * 密码确认
     */
    @NotBlank(message = "密码确认不能为空")
    private String pswdConfim;
}
