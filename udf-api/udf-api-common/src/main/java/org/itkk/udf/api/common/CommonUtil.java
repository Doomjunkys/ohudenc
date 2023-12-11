package org.itkk.udf.api.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.starter.core.exception.AuthException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

/**
 * 工具类
 */
@Slf4j
public class CommonUtil {

    /**
     * 获得用户信息
     *
     * @param request request
     * @return TokenInfoDto
     */
    public static UserDto getUser(HttpServletRequest request) {
        Object tokenInfoDtoObj = request.getAttribute(CommonConstant.PARAMETER_NAME_TOKEN_INFO);
        if (tokenInfoDtoObj == null) {
            throw new AuthException("token信息不存在");
        }
        return (UserDto) tokenInfoDtoObj;
    }

    /**
     * 获得token
     *
     * @param request request
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        Object tokenObj = request.getAttribute(CommonConstant.PARAMETER_NAME_TOKEN);
        if (tokenObj != null) {
            return (String) tokenObj;
        }
        return null;
    }

    /**
     * 从cookie中获得token
     *
     * @param request request
     * @return String
     */
    public static String getTokenByCookie(HttpServletRequest request) {
        if (ArrayUtils.isNotEmpty(request.getCookies())) {
            Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(a -> a.getName().equals(CommonConstant.PARAMETER_NAME_TOKEN)).findFirst();
            if (cookie.isPresent()) {
                return cookie.get().getValue();
            }
        }
        return null;
    }


    /**
     * 私有化构造函数
     */
    private CommonUtil() {

    }
}
