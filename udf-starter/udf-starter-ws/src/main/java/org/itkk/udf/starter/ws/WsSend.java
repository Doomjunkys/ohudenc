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
public class WsSend<T> extends WsBase implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4571263345632468257L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 认证信息
     */
    private String token;

    /**
     * 请求参数
     */
    private T param = null;

    /**
     * 构造函数
     *
     * @return
     */
    public WsSend() {

    }

}
