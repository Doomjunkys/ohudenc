package org.itkk.udf.starter.queue.db;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class DbQueueMessage {
    private String id; //消息ID
    private String hanledBeanName; //处理器bean名称
    private String body; //消息体
}
