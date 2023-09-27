package org.itkk.udf.starter.core.trace;

/**
 * 追踪提醒
 */
public interface ITraceAlert {
    /**
     * 提醒
     *
     * @param traceDto traceDto
     */
    void alert(TraceDto traceDto);
}
