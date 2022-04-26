package org.itkk.udf.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;

@ApiModel(description = "用户鉴权输入模型")
@Data
@Validated
public class UserAuthenticationModel implements Serializable {

    /**
     * 描述 : ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true, dataType = "string")
    @NotBlank
    private String account;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, dataType = "string")
    @NotBlank
    private String password;

    /**
     * 是否单例登录默认false(如果为true,则同一账号,只能同时一个人登录)
     */
    @ApiModelProperty(value = "是否单例登录默认false(如果为true,则同一账号,只能同时一个人登录)", required = false, dataType = "boolean")
    private boolean singleton = false;

    /**
     * 强制登录(只在禁止同时登录的模式下有效)
     */
    @ApiModelProperty(value = "强制登录(只在禁止同时登录的模式下有效)", required = false, dataType = "boolean")
    private boolean forced = false;
}
