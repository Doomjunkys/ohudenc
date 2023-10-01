package org.itkk.udf.starter.cache.db.entity;

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
@TableName("TF_DB_CACHE")
public class DbCacheEntity {
    /**
     * 键(主键)
     */
    @TableId("KEY")
    private String key;
    /**
     * 值
     */
    @TableField("VALUE")
    private String value;
    /**
     * 过期时长(毫秒)
     */
    @TableField("EXPIRE")
    private Long expire;
    /**
     * 创建时间(毫秒)
     */
    @TableField("CREATE_TIME")
    private Long createTime;

    /**
     * 过期时间(毫秒)
     */
    @TableField("EXPIRE_TIME")
    private Long expireTime;

    /**
     * 创建时间
     */
    @TableField("CREATE_DATE")
    private Date createDate;

    /**
     * 更新时间
     */
    @TableField("UPDATE_DATE")
    private Date updateDate;
}
