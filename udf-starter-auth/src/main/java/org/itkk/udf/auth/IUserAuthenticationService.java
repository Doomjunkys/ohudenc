package org.itkk.udf.auth;

import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;

public interface IUserAuthenticationService {

    /**
     * 用户鉴权
     *
     * @param model 用户鉴权输入模型
     * @return 用户鉴权响应模型
     */
    UserAuthenticationResult authentication(UserAuthenticationModel model);

}
