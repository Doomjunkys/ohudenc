/**
 * TokenInvalidException.java
 * Created at 2016-03-01
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.weixin.mp.api;

/**
 * <p>
 * ClassName: KqPosException
 * </p>
 * <p>
 * Description: KqPosException
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2016年3月23日
 * </p>
 */
public class WeixinMpException extends RuntimeException {

    /**
     * <p>
     * Field serialVersionUID: 序列号
     * </p>
     */
    private static final long serialVersionUID = 1L;

    /**
     * <p>
     * Description: 构造函数
     * </p>
     *
     * @param message 异常信息
     */
    public WeixinMpException(String message) {
        super(message);
    }

    /**
     * <p>
     * Description: 构造函数
     * </p>
     *
     * @param cause 堆栈
     */
    public WeixinMpException(Throwable cause) {
        super(cause);
    }

    /**
     * <p>
     * Description: 构造函数
     * </p>
     *
     * @param message 异常信息
     * @param cause   堆栈
     */
    public WeixinMpException(String message, Throwable cause) {
        super(message, cause);
    }

}
