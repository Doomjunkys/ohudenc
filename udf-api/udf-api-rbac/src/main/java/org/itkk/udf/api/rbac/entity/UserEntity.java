package org.itkk.udf.api.rbac.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
@TableName("TM_RBAC_USER")
public class UserEntity {
    /**
     * 主键(PK)(非空)
     */
    @TableId("ID")
    private String id;

    /**
     * 用户ID(UK_1)(非空)
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 用户密码(MD5加密)(非空)
     */
    @TableField("USER_PSWD")
    private String userPswd;

    /**
     * 用户昵称(非空)
     */
    @TableField("NICK_NAME")
    private String nickName;

    /**
     * 用户头像文件ID(非空)
     */
    @TableField("AVATAR_FILE_ID")
    private String avatarFileId;

    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private Date createDate;

    /**
     * 更新人
     */
    @TableField("UPDATE_BY")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField("UPDATE_DATE")
    private Date updateDate;
}
