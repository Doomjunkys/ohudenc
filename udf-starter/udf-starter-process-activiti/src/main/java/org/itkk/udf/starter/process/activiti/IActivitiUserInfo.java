package org.itkk.udf.starter.process.activiti;

import java.util.Set;

public interface IActivitiUserInfo {
    /**
     * 获得用户名称
     *
     * @param userId userId
     * @return String
     */
    String getUserName(String userId);

    /**
     * 获得岗位ID
     *
     * @param userId userId
     * @return Long
     */
    Long getDeptId(String userId);

    /**
     * 获得岗位名称
     *
     * @param userId userId
     * @return String
     */
    String getDeptName(String userId);

    /**
     * 发送邮件
     *
     * @param emailType emailType
     * @param taskId    taskId
     * @param userIds   userIds
     */
    void sendMail(ActivitiConstant.EMAIL_TYPE emailType, String taskId, Set<String> userIds);
}
