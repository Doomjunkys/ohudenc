package org.itkk.udf.starter.core.trace;

import org.itkk.udf.starter.core.CoreUtil;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;

public class TraceMybatisLogImpl extends Slf4jImpl {

    public TraceMybatisLogImpl(String clazz) {
        super(clazz);
    }

    @Override
    public void error(String s) {
        super.error(bindWithTraceId(s));
    }

    @Override
    public void debug(String s) {
        super.debug(bindWithTraceId(s));
    }

    @Override
    public void trace(String s) {
        super.trace(bindWithTraceId(s));
    }

    @Override
    public void warn(String s) {
        super.warn(bindWithTraceId(s));
    }

    /**
     * 日志内容拼接上traceId
     *
     * @param s s
     * @return String
     */
    private String bindWithTraceId(String s) {
        return CoreUtil.getTraceId() + " " + s;
    }

}
