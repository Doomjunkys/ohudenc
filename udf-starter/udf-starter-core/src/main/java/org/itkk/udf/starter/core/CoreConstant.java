package org.itkk.udf.starter.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;
import org.springframework.context.ApplicationContext;

import java.util.Set;

/**
 * 常量
 */
@Slf4j
public class CoreConstant {

    /**
     * spring上下文
     */
    public static ApplicationContext applicationContext;

    /**
     * 运维根路径
     */
    public static final String OPS_PATH_ROOT = "ops/";

    /**
     * 系统参数批量获取参数(字符串)路径
     */
    public static final String PARAMETER_STRING_BATCH_PATH_ROOT = "parameter/string/batch";

    /**
     * 应用名称键
     */
    public static final String APP_NAME_KEY = "appName";

    /**
     * SIGN键
     */
    public static final String SIGN_KEY = "sign";

    /**
     * 环境键
     */
    public static final String REQUEST_PROFILES_ACTIVE_KEY = "profilesActive";

    /**
     * 请求追踪键
     */
    public static final String REQUEST_TRACE_ID_KEY = "traceId";

    /**
     * 请求开始时间键
     */
    public static final String REQUEST_START_TIME_KEY = "startTime";

    /**
     * 请求结束时间键
     */
    public static final String REQUEST_END_TIME_KEY = "endTime";

    /**
     * 请求耗时键
     */
    public static final String REQUEST_TIME_COST_KEY = "timeCost";

    /**
     * 默认字符集
     */
    public static final String CHARACTER_SET = "UTF-8";

    /**
     * 操作成功
     */
    public static final String SUCCESS_MSG = "操作成功";

    /**
     * 操作失败
     */
    public static final String FAIL_MSG = "操作失败";

    /**
     * 参数校验错误
     */
    public static final String PARAMETER_VALID_EXCEPTION_MSG = "参数校验错误";

    /**
     * PDF上下文类型
     */
    public static final String CONTENT_TYPE_PDF = "application/pdf";

    /**
     * DOC上下文类型
     */
    public static final String CONTENT_TYPE_DOC = "application/msword";

    /**
     * DOCX上下文类型
     */
    public static final String CONTENT_TYPE_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";

    /**
     * XLS上下文类型
     */
    public static final String CONTENT_TYPE_XLS = "application/vnd.ms-excel";

    /**
     * XLSX上下文类型
     */
    public static final String CONTENT_TYPE_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    /**
     * PPT上下文类型
     */
    public static final String CONTENT_TYPE_PPT = "application/vnd.ms-powerpoint";

    /**
     * PPTX上下文类型
     */
    public static final String CONTENT_TYPE_PPTX = "application/vnd.openxmlformats-officedocument.presentationml.presentation";

    /**
     * 预览支持的上下文类型
     */
    public static final Set<String> PREVIEW_SUPPORT_CONTENT_TYPE = SetUtils.hashSet(
            CONTENT_TYPE_PDF
    );

    /**
     * OFFICE文件转换PDF支持的上下文类型
     */
    public static final Set<String> OFFICE_CONVERT_PDF_SUPPORT_CONTENT_TYPE = SetUtils.hashSet(
            CONTENT_TYPE_DOC, CONTENT_TYPE_DOCX,
            CONTENT_TYPE_XLS, CONTENT_TYPE_XLSX,
            CONTENT_TYPE_PPT, CONTENT_TYPE_PPTX
    );

    /**
     * 响应状态
     */
    public enum REST_RESPONSE_STATUS {

        /**
         * 成功
         */
        SUCCESS("1"),
        /**
         * 失败
         */
        FIAL("-1");


        private String value;

        REST_RESPONSE_STATUS(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    /**
     * 私有化构造函数
     */
    private CoreConstant() {

    }
}
