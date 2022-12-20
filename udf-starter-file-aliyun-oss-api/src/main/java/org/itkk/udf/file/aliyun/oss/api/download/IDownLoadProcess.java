/**
 * IRabbitmqListener.java
 * Created at 2016-11-17
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.file.aliyun.oss.api.download;

/**
 * 描述 : IDownLoadProcess
 *
 * @author wangkang
 */
public interface IDownLoadProcess<T> {

    /**
     * process
     *
     * @param param param
     * @return String
     */
    String process(DownLoadParam<T> param);

}
