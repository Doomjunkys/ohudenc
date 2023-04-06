package org.itkk.udf.core.domain.weixin.mp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Map;

/**
 * SendMessageTemplateVo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "发送模板消息实体")
public class SendMessageTemplateVo implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * touser
     */
    @ApiModelProperty(value = "接收者openid", required = true, dataType = "string")
    private String touser;

    /**
     * template_id
     */
    @ApiModelProperty(value = "模板ID", required = true, dataType = "string")
    private String template_id; //NOSONAR

    /**
     * url
     */
    @ApiModelProperty(value = "模板跳转链接", dataType = "string")
    private String url;

    /**
     * miniprogram
     */
    @ApiModelProperty(value = "跳小程序所需数据，不需跳小程序可不用传该数据", dataType = "object")
    private Miniprogram miniprogram;

    /**
     * data
     */
    @ApiModelProperty(value = "模板数据", required = true, dataType = "object")
    private Map<String, SendMessageTemplateDataVo> data;

}
