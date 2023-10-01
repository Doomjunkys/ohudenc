package org.itkk.udf.starter.process.activiti.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IActivitRepository {
    /**
     * 更新流程发起人
     *
     * @param procInstId  procInstId
     * @param startUserId startUserId
     */
    void updateProcInstStartUserId(@Param("procInstId") String procInstId, @Param("startUserId") String startUserId);

    /**
     * 根据业务KEY模糊查询流程实例ID
     *
     * @param businessKey businessKey
     * @return List<String>
     */
    List<String> processInstanceIdByBusinessKeyLike(@Param("businessKey") String businessKey);

    /**
     * 根据业务KEY查询流程实例ID
     *
     * @param businessKey businessKey
     * @return String
     */
    String processInstanceIdByBusinessKey(@Param("businessKey") String businessKey);
}
