package org.itkk.udf.starter.ws;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class WsBase implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4571263345632468257L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 路由 (/beanName/methodName)
     */
    private String routing;

    /**
     * 构造函数
     *
     * @return
     */
    public WsBase() {

    }

}
