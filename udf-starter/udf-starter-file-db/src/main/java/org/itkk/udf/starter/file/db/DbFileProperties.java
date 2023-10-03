package org.itkk.udf.starter.file.db;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = DbFileProperties.DB_FILE_PROPERTIES_PRE_FIX)
@Validated
@Data
public class DbFileProperties {

    /**
     * 配置文件前缀
     */
    protected static final String DB_FILE_PROPERTIES_PRE_FIX = "org.itkk.udf.starter.db.file.properties";

    /**
     * 路径映射
     */
    private Map<String, String> mapping;

    /**
     * 支持文件的上传类型
     */
    private String fileUploadSupportType;

    /**
     * 预览支持的上下文类型
     */
    private String previewSupportContentType;

    /**
     * officeOnline地址
     */
    private String officeOnlineHost;

    /**
     * 源地址
     */
    private String officeOnlineWopiSrcHost;

}
