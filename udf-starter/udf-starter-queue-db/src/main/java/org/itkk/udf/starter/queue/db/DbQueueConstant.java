package org.itkk.udf.starter.queue.db;

import lombok.extern.slf4j.Slf4j;

/**
 * 常量
 */
@Slf4j
public class DbQueueConstant {
    /**
     * 响应状态
     */
    public enum DB_QUEUE_STATUS {

        /**
         * 待消费
         */
        PENDING_CONSUMPTION(1),
        /**
         * 已消费
         */
        CONSUMED(2);

        private Integer value;

        DB_QUEUE_STATUS(Integer value) {
            this.value = value;
        }

        public Integer value() {
            return this.value;
        }
    }

    /**
     * 私有化构造函数
     */
    private DbQueueConstant() {

    }
}
