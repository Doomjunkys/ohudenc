package org.itkk.udf.starter.process.activiti;

import lombok.extern.slf4j.Slf4j;

/**
 * 常量
 */
@Slf4j
public class ActivitiConstant {

    /**
     * 流程锁前缀
     */
    public static final String LOCAK_PROC_INST_KEY_PREFIX = "procInst_";

    /**
     * 任务锁前缀
     */
    public static final String LOCAK_TASK_KEY_PREFIX = "task_";

    /**
     * 任务类别
     */
    public enum TASK_CATEGORY {

        /**
         * 不显示
         */
        NOT_DISPLAY;
    }

    /**
     * 响应状态
     */
    public enum EMAIL_TYPE {

        /**
         * 更新代办人
         */
        setAssignee
    }

    /**
     * 私有化构造函数
     */
    private ActivitiConstant() {

    }
}
