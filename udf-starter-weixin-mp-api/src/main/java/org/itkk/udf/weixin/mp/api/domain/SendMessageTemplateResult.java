package org.itkk.udf.weixin.mp.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * SendMessageTemplateResult
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "模板消息发送结果")
public class SendMessageTemplateResult extends BaseResult {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * msgid
     */
    @ApiModelProperty(value = "消息ID", required = true, dataType = "String")
    private String msgid;

}
