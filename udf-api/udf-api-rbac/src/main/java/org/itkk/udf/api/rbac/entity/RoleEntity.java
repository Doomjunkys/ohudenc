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
@TableName("TM_RBAC_ROLE")
public class RoleEntity {

    /**
     * 主键
     */
    @TableId("CODE")
    private String code;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 备注
     */
    @TableField("REMARK")
    private String remark;

    /**
     * 状态 ( -1 : 作废 , 1 : 正常 ) ( 默认 : 正常 )
     */
    @TableField("STATUS")
    private Integer status;

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
