package org.itkk.udf.starter.datasource.dynamic;

/**
 * 描述 : 数据源本地线程
 *
 * @author wangkang
 */
public class DataSourceThreadLocal extends ThreadLocal<String> {
    @Override
    public void remove() {
        super.remove();
        this.initialValue();
    }
}
