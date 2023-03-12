package org.itkk.udf.weixin.mp.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Oauth2Token
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "网页授权用户信息")
public class Oauth2UserInfo extends BaseResult {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * openid
     */
    @ApiModelProperty(value = "用户的唯一标识", required = true, dataType = "string")
    private String openid;

    /**
     * nickname
     */
    @ApiModelProperty(value = "用户昵称", required = true, dataType = "string")
    private String nickname;

    /**
     * sex
     */
    @ApiModelProperty(value = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知", required = true, dataType = "string")
    private String sex;

    /**
     * province
     */
    @ApiModelProperty(value = "用户个人资料填写的省份", required = true, dataType = "string")
    private String province;

    /**
     * city
     */
    @ApiModelProperty(value = "普通用户个人资料填写的城市", required = true, dataType = "string")
    private String city;

    /**
     * country
     */
    @ApiModelProperty(value = " 国家，如中国为CN", required = true, dataType = "string")
    private String country;

    /**
     * headimgurl
     */
    @ApiModelProperty(value = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。", required = true, dataType = "string")
    private String headimgurl;

    /**
     * privilege
     */
    @ApiModelProperty(value = "用户特权信息，json 数组，如微信沃卡用户为（chinaunicom）", required = true, dataType = "array")
    private List<String> privilege;

    /**
     * unionid
     */
    @ApiModelProperty(value = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。", required = true, dataType = "string")
    private String unionid;

}
