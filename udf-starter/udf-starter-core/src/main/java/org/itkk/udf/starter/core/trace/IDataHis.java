package org.itkk.udf.starter.core.trace;

/**
 * 数据历史
 */
public interface IDataHis {
    /**
     * 保存
     *
     * @param traceId traceId
     * @param name    name
     * @param pk      pk
     * @param content content
     */
    void save(String traceId, String name, String pk, Object content, String userId);
}
