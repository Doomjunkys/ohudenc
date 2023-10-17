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
@TableName("TF_DATA_HIS")
public class DataHisEntity {
    /**
     * 主键
     */
    @TableId("ID")
    private String id;
    /**
     * 追踪ID
     */
    @TableField("TRACE_ID")
    private String traceId;
    /**
     * 表名
     */
    @TableField("TABLE_NAME")
    private String TABLE_NAME;
    /**
     * 表主键
     */
    @TableField("TABLE_PK")
    private String TABLE_PK;
    /**
     * 表内容
     */
    @TableField("CONTENT")
    private String content;
    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private Date createDate;
    /**
     * 创建人
     */
    @TableField("CREATE_BY")
    private String createBy;
}
