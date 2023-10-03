package org.itkk.udf.starter.ws;

import org.itkk.udf.starter.core.exception.ParameterValidException;

/**
 * 工具类
 */
public class WsUtil {

    /**
     * 解析路由
     *
     * @param routing routing
     * @return String[]
     */
    public static String[] paseRouting(String routing) {
        //定义返回值
        final String[] rv;
        //判断 & 拆分
        if (routing.startsWith("/")) {
            rv = routing.substring(1).split("/");
        } else {
            rv = routing.split("/");
        }
        //判断长度
        if (rv.length != 2) {
            throw new ParameterValidException("路由不符合规则");
        }
        //返回
        return rv;
    }

    /**
     * 私有化构造函数
     */
    private WsUtil() {

    }
}
