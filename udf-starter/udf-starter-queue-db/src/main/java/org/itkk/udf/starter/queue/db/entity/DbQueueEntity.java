package org.itkk.udf.starter.queue.db.entity;

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
@TableName("TF_DB_QUEUE")
public class DbQueueEntity {
    /**
     * 消息ID
     */
    @TableId("ID")
    private String id;

    /**
     * 应用名称
     */
    @TableField("APPLICATION_NAME")
    private String applicationName;

    /**
     * 当前环境
     */
    @TableField("PROFILES_ACTIVE")
    private String profilesActive;

    /**
     * 队列名称
     */
    @TableField("QUEUE_NAME")
    private String queueName;

    /**
     * 处理器bean名称
     */
    @TableField("HANLED_BEAN_NAME")
    private String hanledBeanName;

    /**
     * 消息体
     */
    @TableField("BODY")
    private String body;

    /**
     * 状态 ( 1 - 待消费 , 2 - 已消费 )
     */
    @TableField("STATUS")
    private Integer status;

    /**
     * 发送时间
     */
    @TableField("SEND_DATE")
    private Date sendDate;

    /**
     * 消费时间
     */
    @TableField("CONSUME_DATE")
    private Date consumeDate;

    /**
     * 消费耗时
     */
    @TableField("CONSUME_COST")
    private Long consumeCost;

    /**
     * 消费消息
     */
    @TableField("CONSUME_MSG")
    private String consumeMsg;

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
