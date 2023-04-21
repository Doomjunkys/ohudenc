package org.itkk.udf.weixin.mp.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.itkk.udf.core.domain.weixin.mp.BaseResult;

import java.util.List;

/**
 * UserInfo
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "微信用户信息")
public class UserInfo extends BaseResult {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * subscribe
     */
    @ApiModelProperty(value = "用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。", required = true, dataType = "int")
    private Integer subscribe;

    /**
     * openid
     */
    @ApiModelProperty(value = "用户的标识，对当前公众号唯一", required = true, dataType = "string")
    private String openid;

    /**
     * nickname
     */
    @ApiModelProperty(value = "用户的昵称", required = true, dataType = "string")
    private String nickname;

    /**
     * sex
     */
    @ApiModelProperty(value = "用户的性别，值为1时是男性，值为2时是女性，值为0时是未知", required = true, dataType = "int")
    private Integer sex;

    /**
     * language
     */
    @ApiModelProperty(value = "用户的语言，简体中文为zh_CN", required = true, dataType = "string")
    private String language;

    /**
     * city
     */
    @ApiModelProperty(value = "用户所在城市", required = true, dataType = "string")
    private String city;

    /**
     * province
     */
    @ApiModelProperty(value = "用户所在省份", required = true, dataType = "string")
    private String province;

    /**
     * country
     */
    @ApiModelProperty(value = "用户所在国家", required = true, dataType = "string")
    private String country;

    /**
     * headimgurl
     */
    @ApiModelProperty(value = "用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。", required = true, dataType = "string")
    private String headimgurl;

    /**
     * subscribe_time
     */
    @ApiModelProperty(value = "用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间", required = true, dataType = "long")
    private Long subscribe_time; //NOSONAR

    /**
     * unionid
     */
    @ApiModelProperty(value = "只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。", required = true, dataType = "string")
    private String unionid;

    /**
     * remark
     */
    @ApiModelProperty(value = "公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注", required = true, dataType = "string")
    private String remark;

    /**
     * groupid
     */
    @ApiModelProperty(value = "用户所在的分组ID（兼容旧的用户分组接口）", required = true, dataType = "int")
    private Integer groupid;

    /**
     * tagid_list
     */
    @ApiModelProperty(value = "用户被打上的标签ID列表", required = true, dataType = "array")
    private List<Integer> tagid_list; //NOSONAR

}
