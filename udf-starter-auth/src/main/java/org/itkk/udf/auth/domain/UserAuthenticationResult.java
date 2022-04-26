package org.itkk.udf.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * UserAuthenticationResult
 */
@ApiModel(description = "用户鉴权响应模型")
@Data
@AllArgsConstructor
public class UserAuthenticationResult {
    /**
     * 授权token
     */
    @ApiModelProperty(value = "授权token", required = true, dataType = "string")
    private String accessToken;
}
