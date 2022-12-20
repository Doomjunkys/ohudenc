/**
 * BaseMqConstant.java
 * Created at 2016-12-05
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package org.itkk.udf.file.aliyun.oss.api.download;

/**
 * 描述 : DownConstant
 *
 * @author Administrator
 */
public class DownConstant {

    /**
     * 下载状态
     */
    public enum DOWNLOAD_PROCESS_STATUS {
        /**
         * 执行中
         */
        STATUS_1(1),
        /**
         * 执行完成
         */
        STATUS_2(2),
        /**
         * 执行错误
         */
        STATUS_3(3);

        /**
         * value
         */
        private Integer value;

        /**
         * PAY_TYPE
         *
         * @param value value
         */
        DOWNLOAD_PROCESS_STATUS(Integer value) {
            this.value = value;
        }

        /**
         * value
         *
         * @return Integer
         */
        public Integer value() {
            return this.value;
        }
    }

    /**
     * 描述 : 私有化构造函数
     */
    private DownConstant() {
    }

}
