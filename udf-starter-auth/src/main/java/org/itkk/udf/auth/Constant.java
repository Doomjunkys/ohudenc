/**
 * Constant.java
 * Created at 2017-04-24
 * Created by Administrator
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.auth;

/**
 * 描述 : 常量
 *
 * @author Administrator
 */
public class Constant {

    /**
     * 描述 : 头信息(rms应用名称)
     */
    public static final String HEADER_USER_TYPE = "userType";

    /**
     * 身份认证顺序
     */
    public static final int ORDER_IDENTITY = 10000;

    /**
     * 权限认证顺序
     */
    public static final int ORDER_PURVIEW = 10001;

    /**
     * rms认证顺序
     */
    public static final int ORDER_RMS = 10002;


    /**
     * 描述 : 私有化构造函数
     */
    private Constant() {

    }

}
