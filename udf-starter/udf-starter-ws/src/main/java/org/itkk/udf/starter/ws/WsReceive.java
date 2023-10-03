package org.itkk.udf.starter.ws;

import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.ErrorDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class WsReceive<T> extends WsBase implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4571263345632468257L;

    /**
     * 状态码
     */
    private String code = WsConstant.WS_RESPONSE_STATUS.SUCCESS.value();

    /**
     * 结果集
     */
    private T result = null;

    /**
     * 错误详情
     */
    private ErrorDetail errorDetail;

    /**
     * 构造函数
     *
     * @return
     */
    public WsReceive() {

    }

    /**
     * 构造函数
     *
     * @param result 结果集
     */
    public WsReceive(T result) {
        this.code = WsConstant.WS_RESPONSE_STATUS.SUCCESS.value();
        this.result = result;
    }

    /**
     * 构造函数
     *
     * @param code   状态码
     * @param result 结果集
     */
    public WsReceive(WsConstant.WS_RESPONSE_STATUS code, T result) {
        this.code = code.value();
        this.result = result;
    }

    /**
     * 操作成功
     *
     * @return
     */
    public static WsReceive<String> success() {
        return new WsReceive<>(WsConstant.WS_RESPONSE_STATUS.SUCCESS, CoreConstant.SUCCESS_MSG);
    }

}
