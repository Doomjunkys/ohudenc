package org.itkk.udf.starter.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class RestResponse<T> implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 4571263345632468257L;

    /**
     * 状态码
     */
    private String code = CoreConstant.REST_RESPONSE_STATUS.SUCCESS.value();

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
    public RestResponse() {

    }

    /**
     * 构造函数
     *
     * @param result 结果集
     */
    public RestResponse(T result) {
        this.code = CoreConstant.REST_RESPONSE_STATUS.SUCCESS.value();
        this.result = result;
    }

    /**
     * 构造函数
     *
     * @param code   状态码
     * @param result 结果集
     */
    public RestResponse(CoreConstant.REST_RESPONSE_STATUS code, T result) {
        this.code = code.value();
        this.result = result;
    }

    /**
     * 操作成功
     *
     * @return
     */
    public static RestResponse<String> success() {
        return new RestResponse<>(CoreConstant.REST_RESPONSE_STATUS.SUCCESS, CoreConstant.SUCCESS_MSG);
    }

}
