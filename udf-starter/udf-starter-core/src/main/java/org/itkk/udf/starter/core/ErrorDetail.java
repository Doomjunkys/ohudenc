package org.itkk.udf.starter.core;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@Accessors(chain = true)
@ToString
@EqualsAndHashCode(callSuper = false)
public class ErrorDetail implements Serializable {
    /**
     * ID
     */
    private static final long serialVersionUID = 4277853186438467877L;

    /**
     * 时间
     */
    private Date date;

    /**
     * 类型
     */
    private String type;

    /**
     * 消息
     */
    private String message;

    /**
     * 堆栈
     */
    private String stackTrace;

    /**
     * 子错误
     */
    private RestResponse<String> child;

}
