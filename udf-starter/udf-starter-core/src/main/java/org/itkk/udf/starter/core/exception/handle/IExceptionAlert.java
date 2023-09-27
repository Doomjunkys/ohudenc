package org.itkk.udf.starter.core.exception.handle;

/**
 * 异常通知
 */
public interface IExceptionAlert {
    /**
     * 通知
     *
     * @param traceId traceId
     * @param e e
     */
    void alert(String traceId,Exception e);
}
