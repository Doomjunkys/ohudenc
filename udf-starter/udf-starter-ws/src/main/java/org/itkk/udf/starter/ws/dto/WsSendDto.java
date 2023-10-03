package org.itkk.udf.starter.ws.dto;

import org.itkk.udf.starter.ws.WsReceive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class WsSendDto implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4571263345632468257L;

    /**
     * 用户ID列表
     */
    private List<String> userIds;

    /**
     * 消息
     */
    private WsReceive<Object> receive;

    /**
     * 构造函数
     *
     * @return
     */
    public WsSendDto() {

    }

}
