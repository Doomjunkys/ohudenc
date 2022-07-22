package org.itkk.udf.id.service;

import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.itkk.udf.id.IdWorkerInit;
import org.itkk.udf.id.IdWorkerProperties;
import org.itkk.udf.id.domain.CacheValue;
import org.itkk.udf.id.domain.Id;
import org.itkk.udf.rms.Rms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IdWorkerService
 */
@Service
public class IdWorkerService {

    /**
     * idWorker
     */
    @Autowired
    private IdWorkerInit idWorkerInit;

    /**
     * idWorkerProperties
     */
    @Autowired
    private IdWorkerProperties idWorkerProperties;

    /**
     * rms
     */
    @Autowired
    private Rms rms;

    /**
     * 获得分布式ID[IdWorker随机]
     *
     * @return ID
     */
    public String get() {
        return Long.toString(idWorkerInit.get().nextId());
    }

    /**
     * 批量获得分布式ID[IdWorker随机]
     *
     * @param count 数量
     * @return ID
     */
    public List<String> get(Integer count) {
        //判断最大数量
        if (count > idWorkerProperties.getMaxCount()) {
            throw new SystemRuntimeException("The number is too large and the maximum setting is " + idWorkerProperties.getMaxCount());
        }
        //循环生成
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            ids.add(Long.toString(idWorkerInit.get().nextId()));
        }
        return ids;
    }

    /**
     * 解析分布式ID
     *
     * @param id 分布式ID
     * @return ID
     */
    public Id reverse(long id) {
        return idWorkerInit.get().reverse(id);
    }

    /**
     * 获得分布式ID[指定IdWorker]
     *
     * @param datacenterId 数据中心ID
     * @param workerId     workerId
     * @return ID
     */
    public String workerGet(Integer datacenterId, Integer workerId) {
        //定义需要请求的接口
        final String serviceCode = "ID_1";
        //获得缓存
        CacheValue cacheValue = idWorkerInit.getCacheValue(datacenterId, workerId);
        //远程请求
        ResponseEntity<RestResponse<String>> result = rms.call(cacheValue.getHost(), cacheValue.getPort(), serviceCode, null, null, new ParameterizedTypeReference<RestResponse<String>>() {
        }, null);
        //返回
        return result.getBody().getResult();
    }

    /**
     * 批量获得分布式ID[指定IdWorker]
     *
     * @param datacenterId 数据中心ID
     * @param workerId     workerId
     * @param count        数量
     * @return ID
     */
    public List<String> workerGet(Integer datacenterId, Integer workerId, Integer count) {
        //定义需要请求的接口
        final String serviceCode = "ID_2";
        //获得缓存
        CacheValue cacheValue = idWorkerInit.getCacheValue(datacenterId, workerId);
        //构建uriVariables
        Map<String, Object> uriVariables = new HashMap<>();
        uriVariables.put("count", count);
        //远程请求
        ResponseEntity<RestResponse<List<String>>> result = rms.call(cacheValue.getHost(), cacheValue.getPort(), serviceCode, null, null, new ParameterizedTypeReference<RestResponse<List<String>>>() {
        }, uriVariables);
        //返回
        return result.getBody().getResult();
    }

}
