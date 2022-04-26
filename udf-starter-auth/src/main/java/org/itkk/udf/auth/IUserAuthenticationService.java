package org.itkk.udf.auth;

/**
 * IUserAuthenticationService
 */
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
     * @param account   账号
     * @param password  密码
     * @param singleton 是否单例
     * @param forced     是否强制
     * @return accessToken
     */
    String authentication(String account, String password, boolean singleton, boolean forced);

    /**
     * toke校验
     * <p>
     * 需要处理单例登录的情况
     * </p>
     * <p>
     * 在单例登录的情况下,需要处理强制登录的情况
     * </p>
     *
     * @param accessToken accessToken
     */
    void verification(String accessToken);

}
