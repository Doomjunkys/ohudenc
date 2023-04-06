package org.itkk.udf.core.domain.weixin.mp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * SendMessageTemplateDataVo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "发送模板消息变量Data实体")
public class SendMessageTemplateDataVo implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * value
     */
    @ApiModelProperty(value = "变量的数据", required = true, dataType = "string")
    private String value;

    /**
     * color
     */
    @ApiModelProperty(value = "模板内容字体颜色，不填默认为黑色", dataType = "string")
    private String color;

}
