package org.itkk.udf.weixin.mp.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * BaseResult
 */
@Data
@ApiModel(description = "微信基础返回信息")
public class BaseResult implements Serializable {
    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * errcode
     */
    @ApiModelProperty(value = "错误代码", required = true, dataType = "string")
    private String errcode;

    /**
     * errmsg
     */
    @ApiModelProperty(value = "错误描述", required = true, dataType = "string")
    private String errmsg;

}
