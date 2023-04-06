package org.itkk.udf.weixin.mp.api.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.itkk.udf.core.domain.weixin.mp.BaseResult;

/**
 * AccessToken
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AccessToken extends BaseResult {
    /**
     * ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 凭证
     */
    private String access_token; //NOSONAR

    /**
     * 凭证有效期
     */
    private Integer expires_in; //NOSONAR

}
