package org.itkk.udf.api.common.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.itkk.udf.api.common.service.DataHisService;
import org.itkk.udf.api.common.service.TraceInfoService;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.itkk.udf.starter.core.exception.handle.IExceptionAlert;
import org.itkk.udf.starter.core.trace.IDataHis;
import org.itkk.udf.starter.core.trace.ITraceAlert;
import org.itkk.udf.starter.core.trace.TraceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DbLogImpl implements ITraceAlert, IExceptionAlert, IDataHis {

    /**
     * traceInfoService
     */
    @Autowired
    private TraceInfoService traceInfoService;

    /**
     * dataHisService
     */
    @Autowired
    private DataHisService dataHisService;

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    @Override
    public void save(String traceId, String name, String pk, Object content, String userId) {
        try {
            dataHisService.save(traceId, name, pk, internalObjectMapper.writeValueAsString(content), userId);
        } catch (JsonProcessingException ex) {
            throw new SystemRuntimeException(ex);
        }
    }

    @Override
    @Async
    public void alert(String traceId, Exception e) {
        try {
            final String type = "IExceptionAlert";
            traceInfoService.save(traceId, type, internalObjectMapper.writeValueAsString(ExceptionUtils.getStackTrace(e)));
        } catch (JsonProcessingException ex) {
            log.warn(traceId + " (IExceptionAlert)记录trace日志失败,错误信息为 : " + ex.getMessage());
        }
    }

    @Override
    @Async
    public void alert(TraceDto traceDto) {
        try {
            //定义常量
            final String type = "ITraceAlert";
            //排除OPTIONS请求
            if (!HttpMethod.OPTIONS.toString().toUpperCase().equals(traceDto.getMethod().toUpperCase())) {
                //保存
                traceInfoService.save(traceDto.getTraceId(), type, internalObjectMapper.writeValueAsString(traceDto));
            }
        } catch (JsonProcessingException e) {
            log.warn(traceDto.getTraceId() + " (ITraceAlert)记录trace日志失败,错误信息为 : " + e.getMessage());
        }
    }
}
