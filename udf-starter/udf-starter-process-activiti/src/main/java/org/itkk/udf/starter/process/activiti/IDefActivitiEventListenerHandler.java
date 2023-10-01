package org.itkk.udf.starter.process.activiti;

import org.activiti.engine.delegate.event.ActivitiEvent;

public interface IDefActivitiEventListenerHandler {
    /**
     * 事件处理
     *
     * @param event event
     */
    void handle(ActivitiEvent event);
}
