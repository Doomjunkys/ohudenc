package org.itkk.udf.starter.queue.db.service;

import org.itkk.udf.starter.queue.db.DbQueueConstant;
import org.itkk.udf.starter.queue.db.DbQueueMessage;
import org.itkk.udf.starter.queue.db.IDbQueueLock;
import org.itkk.udf.starter.queue.db.entity.DbQueueEntity;
import org.itkk.udf.starter.queue.db.repository.IDbQueueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DbQueueService {

    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String profilesActive;

    /**
     * 当前应用名称
     */
    @Value("${spring.application.name}")
    private String applicationName;

    /**
     * iDbQueueRepository
     */
    @Autowired
    private IDbQueueLock iDbQueueLock;

    /**
     * iDbQueueRepository
     */
    @Autowired
    private IDbQueueRepository iDbQueueRepository;

    /**
     * 清理过期的缓存
     */
    public void clearDbQueue() {
        iDbQueueRepository.clearDbQueue(applicationName, profilesActive);
    }

    /**
     * 更新任务状态为已消费
     *
     * @param id          消息ID
     * @param consumeDate 消费时间
     * @param onsumeCost  消费耗时
     * @param consumMsg   消费消息
     */
    public void updateToConsumed(String id, Date consumeDate, long onsumeCost, String consumMsg) {
        //构造更新实体
        DbQueueEntity updateEntity = new DbQueueEntity();
        updateEntity.setId(id);
        updateEntity.setStatus(DbQueueConstant.DB_QUEUE_STATUS.CONSUMED.value());
        updateEntity.setConsumeDate(consumeDate);
        updateEntity.setConsumeCost(onsumeCost);
        updateEntity.setConsumeMsg(consumMsg);
        updateEntity.setUpdateDate(new Date());
        //更新
        iDbQueueRepository.updateById(updateEntity);
    }

    /**
     * 获得任务详情
     *
     * @param id
     * @return
     */
    public DbQueueEntity get(String id) {
        return iDbQueueRepository.selectById(id);
    }

    /**
     * 获取带消费的消息列表
     *
     * @param queueName 队列名称
     * @param limit     行数
     * @return 消息列表
     */
    public List<String> loadPendingConsumptionList(String queueName, Integer limit) {
        return iDbQueueRepository.loadPendingConsumptionList(applicationName, profilesActive, queueName, limit);
    }

    /**
     * 消息入库
     *
     * @param queueName      队列名称
     * @param dbQueueMessage 消息
     */
    public void add(String queueName, DbQueueMessage dbQueueMessage) {
        //构造消息实体
        DbQueueEntity dbQueueEntity = new DbQueueEntity();
        dbQueueEntity.setId(dbQueueMessage.getId());
        dbQueueEntity.setApplicationName(applicationName);
        dbQueueEntity.setProfilesActive(profilesActive);
        dbQueueEntity.setQueueName(queueName);
        dbQueueEntity.setHanledBeanName(dbQueueMessage.getHanledBeanName());
        dbQueueEntity.setBody(dbQueueMessage.getBody());
        dbQueueEntity.setStatus(DbQueueConstant.DB_QUEUE_STATUS.PENDING_CONSUMPTION.value());
        dbQueueEntity.setSendDate(new Date());
        dbQueueEntity.setCreateDate(new Date());
        dbQueueEntity.setUpdateDate(new Date());
        //入库
        iDbQueueRepository.insert(dbQueueEntity);
    }
}
