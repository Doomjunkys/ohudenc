package org.itkk.udf.file.aliyun.oss.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * PresignedUrlParam
 */
@ApiModel(description = "邮件信息")
@Data
public class PresignedUrlParam implements Serializable {

    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * code
     */
    @ApiModelProperty(value = "阿里云oss的配置代码", required = true, dataType = "string")
    @NotBlank(message = "阿里云oss的配置代码不能为空")
    private String code;

    /**
     * objectKey
     */
    @ApiModelProperty(value = "阿里云oss的的对象key", required = true, dataType = "string")
    @NotBlank(message = "阿里云oss的的对象key不能为空")
    private String objectKey;
}
