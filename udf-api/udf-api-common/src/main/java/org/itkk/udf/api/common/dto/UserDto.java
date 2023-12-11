package org.itkk.udf.api.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public class UserDto implements Serializable {
    /**
     * ID
     */
    private static final long serialVersionUID = -6075064116058975444L;

    /**
     * 主键(PK)(非空)
     */
    private String id;

    /**
     * 用户ID(UK_1)(非空)
     */
    private String userId;

    /**
     * 用户昵称(非空)
     */
    private String nickName;

    /**
     * 用户头像文件ID
     */
    private String avatarFileId;

    /**
     * 用户头像路径
     */
    private String avatarFilePath;

    /**
     * 用户状态 (1:正常)
     */
    private Integer status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private Date updateDate;
}
