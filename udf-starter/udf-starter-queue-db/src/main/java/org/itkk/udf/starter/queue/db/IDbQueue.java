package org.itkk.udf.starter.queue.db;

public interface IDbQueue {
    /**
     * 生产消息
     *
     * @param dbQueueMessage 消息
     * @return String 消息ID
     */
    String producer(DbQueueMessage dbQueueMessage);
}
