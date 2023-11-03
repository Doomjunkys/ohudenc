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
public class UserSaveDto implements Serializable {
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
     * 用户昵称(非空)
     */
    @NotBlank(message = "昵称不能为空")
    @Length(max = 50, message = "昵称长度不能超过50个字符")
    private String nickName;

    /**
     * 用户头像文件ID(非空)
     */
    private String avatarFileId;
}
