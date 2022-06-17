package org.itkk.udf.scheduler.impl;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.scheduler.IRmsJobEvent;
import org.itkk.udf.scheduler.client.domain.RmsJobParam;
import org.itkk.udf.scheduler.client.domain.RmsJobResult;

/**
 * DefRmsJobEvent
 */
@Slf4j
public class DefRmsJobEvent implements IRmsJobEvent {

    @Override
    public boolean hasRunning(RmsJobParam rmsJobParam) {
        log.info("----- DefRmsJobEvent.hasRunning -----> {}", rmsJobParam.toString());
        return false;
    }

    @Override
    public void save(RmsJobParam param, RmsJobResult result) {
        if (param != null) {
            log.info("----- DefRmsJobEvent.save -----> {}", param.toString());
        }
        if (result != null) {
            log.info("----- DefRmsJobEvent.save -----> {}", result.toString());
        }
    }

    @Override
    public void delete(String fireInstanceId) {
        log.info("----- DefRmsJobEvent.delete -----> {}", fireInstanceId);
    }
}
