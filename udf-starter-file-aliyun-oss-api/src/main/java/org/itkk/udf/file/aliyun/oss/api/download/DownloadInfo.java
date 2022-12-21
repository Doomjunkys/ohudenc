package org.itkk.udf.file.aliyun.oss.api.download;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DownloadInfo
 */
@ApiModel(description = "文件下载信息")
@Data
public class DownloadInfo {

    /**
     * id
     */
    @ApiModelProperty(value = "下载ID", required = true, dataType = "string")
    private String id;

    /**
     * status
     */
    @ApiModelProperty(value = "下载状态 ( 1 : 待执行 , 2 : 执行中 , 3 : 执行完成 , 4 : 执行错误 )", required = true, dataType = "int")
    private Integer status;

    /**
     * objectKey
     */
    @ApiModelProperty(value = "阿里云OSS的objectKey", required = true, dataType = "string")
    private String objectKey;

    /**
     * errorMsg
     */
    @ApiModelProperty(value = "错误信息", required = true, dataType = "string")
    private String errorMsg;
}
