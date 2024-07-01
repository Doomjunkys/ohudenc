package org.itkk.udf.starter.core;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = CoreProperties.CORE_PROPERTIES_PRE_FIX)
@Validated
@Data
public class CoreProperties implements ApplicationContextAware {

    /**
     * 配置文件前缀
     */
    protected static final String CORE_PROPERTIES_PRE_FIX = "org.itkk.udf.starter.core.properties";

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CoreConstant.applicationContext = applicationContext;
    }

    /**
     * 是否缓存上下文
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".contentCaching不能为空")
    private Boolean contentCaching = true;

    /**
     * 上下文排除
     */
    private String[] contentExclude;

    /**
     * 跨域根路径
     */
    @NotBlank(message = CORE_PROPERTIES_PRE_FIX + ".corsPathPattern不能为空")
    private String corsPathPattern = "/**";

    /**
     * 跨域允许的域
     */
    @NotBlank(message = CORE_PROPERTIES_PRE_FIX + ".corsAllowedOrigins不能为空")
    private String corsAllowedOrigins;

    /**
     * 追踪过滤器根路径(Filter)
     */
    @NotBlank(message = CORE_PROPERTIES_PRE_FIX + ".xssFilterPathPattern不能为空")
    private String xssFilterPathPattern = "/*";

    /**
     * 追踪过滤器根路径(Filter)
     */
    @NotBlank(message = CORE_PROPERTIES_PRE_FIX + ".traceFilterPathPattern不能为空")
    private String traceFilterPathPattern = "/*";

    /**
     * 是否输出异常堆栈
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".outputExceptionStackTrace不能为空")
    private boolean outputExceptionStackTrace = true;

    /**
     * http连接超时时间
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".httpConnectTimeout不能为空")
    private Integer httpConnectTimeout = 5000;

    /**
     * http读取超时时间
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".httpReadTimeout不能为空")
    private Integer httpReadTimeout = 5000;

    /**
     * RestTemplate是否允许大消息体
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".bufferRequestBody不能为空")
    private Boolean bufferRequestBody = true;

    /**
     * 默认时区
     */
    @NotBlank(message = CORE_PROPERTIES_PRE_FIX + ".timeZone不能为空")
    private String timeZone = "Asia/Shanghai";

    /**
     * 默认日期格式
     */
    @NotBlank(message = CORE_PROPERTIES_PRE_FIX + ".dateFormat不能为空")
    private String dateFormat = "yyyy-MM-dd'T'HH:mm:ss:SSSZ";

    /**
     * 是否追踪提醒
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".traceAlert不能为空")
    private Boolean traceAlert = true;

    /**
     * 异常日志输出排除
     */
    private String exceptionDetailLogExclude = "org.itkk.udf.starter.core.exception.AuthException" +
            ",org.itkk.udf.starter.core.exception.ParameterValidException" +
            ",org.itkk.udf.starter.core.exception.PermissionException" +
            ",org.springframework.web.bind.MethodArgumentNotValidException";

    /**
     * 文件锁根目录
     */
    private String fileLockRootDir;

    /**
     * Guava缓存
     */
    @NotNull(message = CORE_PROPERTIES_PRE_FIX + ".guavaCacheProperties不能为空")
    private GuavaCacheProperties guavaCacheProperties = new GuavaCacheProperties();

    /**
     * Guava缓存配置属性
     */
    @Validated
    @Data
    public static class GuavaCacheProperties {
        /**
         * 并发级别
         */
        private Integer concurrencyLevel = 10;
        /**
         * 缓存容器的初始容量
         */
        private Integer initialCapacity = 10;
        /**
         * 缓存最大容量
         */
        private Integer maximumSize = 100;
        /**
         * 过期时间
         */
        private Integer expireAfterWrite = 10;
    }
}
