package org.itkk.udf.auth;

import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;

public interface IUserAuthenticationService {

    /**
     * 用户鉴权
     * <p>
     * 需要处理单例登录的情况
     * </p>
     * <p>
     * 在单例登录的情况下,需要处理强制登录的情况
     * </p>
     *
     * @param model 用户鉴权输入模型
     * @return 用户鉴权响应模型
     */
    UserAuthenticationResult authentication(UserAuthenticationModel model);

}
