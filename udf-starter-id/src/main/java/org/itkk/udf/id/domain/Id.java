package org.itkk.udf.id.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Id
 */
@Data
@ApiModel(description = "分布式ID信息")
public class Id implements Serializable {
    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 机器ID( 0 - 31 )
     */
    @ApiModelProperty(value = "机器ID( 0 - 31 )", required = true, dataType = "long")
    private long workerId;

    /**
     * 数据中心ID( 0 - 31 )
     */
    @ApiModelProperty(value = "数据中心ID( 0 - 31 )", required = true, dataType = "long")
    private long datacenterId;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳", required = true, dataType = "long")
    private long timestamp;

    /**
     * 序列号
     */
    @ApiModelProperty(value = "序列号", required = true, dataType = "long")
    private long sequence;
}