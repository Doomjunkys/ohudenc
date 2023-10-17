package org.itkk.udf.api.common.service;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.entity.DataHisEntity;
import org.itkk.udf.api.common.repository.IDataHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class DataHisService {

    /**
     * iDataHisRepository
     */
    @Autowired
    private IDataHisRepository iDataHisRepository;

    /**
     * 保存
     *
     * @param traceId traceId
     * @param name    name
     * @param pk      pk
     * @param content content
     * @param userId  userId
     */
    public void save(String traceId, String name, String pk, String content, String userId) {
        DataHisEntity entity = new DataHisEntity();
        entity.setId(UUID.randomUUID().toString());
        entity.setTraceId(traceId);
        entity.setTABLE_NAME(name);
        entity.setTABLE_PK(pk);
        entity.setContent(content);
        entity.setCreateDate(new Date());
        entity.setCreateBy(userId);
        iDataHisRepository.insert(entity);
    }
}
