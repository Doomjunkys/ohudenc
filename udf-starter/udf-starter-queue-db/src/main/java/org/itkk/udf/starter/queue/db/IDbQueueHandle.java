package org.itkk.udf.starter.queue.db;

public interface IDbQueueHandle {

    /**
     * 处理任务
     *
     * @param id   id
     * @param body body
     */
    void handle(String id, String body);
}
