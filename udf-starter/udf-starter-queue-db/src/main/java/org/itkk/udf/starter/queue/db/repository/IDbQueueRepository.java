package org.itkk.udf.starter.queue.db.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.itkk.udf.starter.queue.db.entity.DbQueueEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IDbQueueRepository extends BaseMapper<DbQueueEntity> {
    /**
     * 加载[待消费]列表
     *
     * @param applicationName applicationName
     * @param profilesActive  profilesActive
     * @param queueName       queueName
     * @param limit           limit
     * @return List<String>
     */
    List<String> loadPendingConsumptionList(
            @Param("applicationName") String applicationName,
            @Param("profilesActive") String profilesActive,
            @Param("queueName") String queueName,
            @Param("limit") Integer limit
    );

    /**
     * 清理表数据
     *
     * @param applicationName applicationName
     * @param profilesActive  profilesActive
     */
    void clearDbQueue(
            @Param("applicationName") String applicationName,
            @Param("profilesActive") String profilesActive
    );
}
