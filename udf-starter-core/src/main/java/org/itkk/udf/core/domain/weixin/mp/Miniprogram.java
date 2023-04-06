package org.itkk.udf.core.domain.weixin.mp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * Miniprogram
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "小程序")
public class Miniprogram implements Serializable {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * appid
     */
    @ApiModelProperty(value = "所需跳转到的小程序appid（该小程序appid必须与发模板消息的公众号是绑定关联关系）", required = true, dataType = "string")
    private String appid;

    /**
     * pagepath
     */
    @ApiModelProperty(value = "所需跳转到小程序的具体页面路径，支持带参数,（示例index?foo=bar）", required = true, dataType = "string")
    private String pagepath;

}
