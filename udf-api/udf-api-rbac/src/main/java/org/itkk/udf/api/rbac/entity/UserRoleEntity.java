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
@TableName("TM_RBAC_USER_ROLE")
public class UserRoleEntity {

    /**
     * 主键ID
     */
    @TableId("ID")
    private String id;

    /**
     * 用户ID (工号) ( UK_1 )
     */
    @TableField("USER_ID")
    private String userId;

    /**
     * 角色代码 ( UK_1 )
     */
    @TableField("ROLE_CODE")
    private String roleCode;

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
