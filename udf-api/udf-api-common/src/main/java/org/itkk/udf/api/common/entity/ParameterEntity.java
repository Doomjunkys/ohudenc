package org.itkk.udf.api.common.entity;

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
@TableName("TM_PARAMETER")
public class ParameterEntity {

    /**
     * 代码
     */
    @TableId("CODE")
    private String code;

    /**
     * 类别
     */
    @TableField("TYPE")
    private String type;

    /**
     * 名称
     */
    @TableField("NAME")
    private String name;

    /**
     * 内容
     */
    @TableField("CONTENT")
    private String content;

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
