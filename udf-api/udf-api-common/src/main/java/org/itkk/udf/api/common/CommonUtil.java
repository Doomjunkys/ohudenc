package org.itkk.udf.api.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.starter.core.exception.AuthException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

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
        Object userDtoObj = request.getAttribute(CommonConstant.PARAMETER_NAME_TOKEN_INFO);
        if (userDtoObj == null) {
            throw new AuthException("userDto信息不存在");
        }
        return (UserDto) userDtoObj;
    }

    /**
     * 获得token
     *
     * @param request request
     * @return String
     */
    public static String getToken(HttpServletRequest request) {
        //从url参数获得
        String token = request.getParameter(CommonConstant.PARAMETER_NAME_TOKEN);
        if (StringUtils.isBlank(token)) {
            //从header获得
            token = request.getHeader(CommonConstant.PARAMETER_NAME_TOKEN);
            //从作用域中获得
            if (StringUtils.isBlank(token)) {
                token = request.getAttribute(CommonConstant.PARAMETER_NAME_TOKEN) == null ? null : request.getAttribute(CommonConstant.PARAMETER_NAME_TOKEN).toString();
                //从cookie中获得
                if (StringUtils.isBlank(token)) {
                    token = ArrayUtils.isEmpty(request.getCookies()) ? null : Arrays.stream(request.getCookies()).filter(a -> a.getName().equals(CommonConstant.PARAMETER_NAME_TOKEN)).map(Cookie::getValue).findFirst().orElse(null);
                    //从其他地方获得
                    if (StringUtils.isBlank(token)) {
                        //从其他地方获得
                    }
                }
            }
        }
        //返回
        return token;
    }

    /**
     * 设置cookie
     *
     * @param httpServletResponse httpServletResponse
     * @param name                name
     * @param value               value
     */
    public static void setCookie(HttpServletResponse httpServletResponse, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(false);
        httpServletResponse.addCookie(cookie);
    }

    /**
     * 私有化构造函数
     */
    private CommonUtil() {

    }

    public static void main(String[] args) {
        String[] a = null;
        Arrays.stream(a);
    }
}
