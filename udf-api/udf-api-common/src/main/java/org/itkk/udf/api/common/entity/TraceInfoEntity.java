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
@TableName("TF_TRACE_INFO")
public class TraceInfoEntity {
    /**
     * 主键
     */
    @TableId("ID")
    private String ID;
    /**
     * 追踪ID
     */
    @TableField("TRACE_ID")
    private String traceId;
    /**
     * 类型
     */
    @TableField("TYPE")
    private String type;
    /**
     * 追踪消息体
     */
    @TableField("CONTENT")
    private String content;
    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private Date createDate;
}
