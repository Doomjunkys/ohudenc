package org.itkk.udf.api.common.service;


import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.CommonProperties;
import org.itkk.udf.api.common.entity.TraceInfoEntity;
import org.itkk.udf.api.common.repository.ITraceInfoRepository;
import org.itkk.udf.starter.cache.db.service.DbCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class TraceInfoService {
    /**
     * iTraceInfoRepository
     */
    @Autowired
    private ITraceInfoRepository iTraceInfoRepository;

    /**
     * dbCacheService
     */
    @Autowired
    private DbCacheService dbCacheService;

    /**
     * commonProperties
     */
    @Autowired
    private CommonProperties commonProperties;

    /**
     * 定时清理追踪日志
     */
    public void clearTraceInfo() {
        iTraceInfoRepository.clearTraceInfo(commonProperties.getTraceLogKeepDays());
    }

    /**
     * 保存log(异步)
     *
     * @param traceId traceId
     * @param type    type
     * @param content content
     */
    @Async
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void save(String traceId, String type, String content) {
        TraceInfoEntity traceInfoEntity = new TraceInfoEntity();
        traceInfoEntity.setID(UUID.randomUUID().toString());
        traceInfoEntity.setTraceId(traceId);
        traceInfoEntity.setType(type);
        traceInfoEntity.setContent(content);
        traceInfoEntity.setCreateDate(new Date());
        iTraceInfoRepository.insert(traceInfoEntity);
    }
}
