package org.itkk.udf.weixin.mp.api.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Oauth2Token
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "网页授权token信息")
public class Oauth2Token extends BaseResult {

    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * access_token
     */
    @ApiModelProperty(value = "网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同", required = true, dataType = "string")
    private String access_token; //NOSONAR

    /**
     * expires_in
     */
    @ApiModelProperty(value = "ccess_token接口调用凭证超时时间，单位（秒）", required = true, dataType = "int")
    private Integer expires_in; //NOSONAR

    /**
     * refresh_token
     */
    @ApiModelProperty(value = "用户刷新access_token", required = true, dataType = "string")
    private String refresh_token; //NOSONAR

    /**
     * openid
     */
    @ApiModelProperty(value = "用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID", required = true, dataType = "string")
    private String openid;

    /**
     * scope
     */
    @ApiModelProperty(value = "用户授权的作用域，使用逗号（,）分隔", required = true, dataType = "string")
    private String scope;

}
