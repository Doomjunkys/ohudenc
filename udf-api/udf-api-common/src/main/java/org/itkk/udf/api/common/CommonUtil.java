package org.itkk.udf.api.common;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.api.common.dto.UserDto;
import org.itkk.udf.starter.core.exception.AuthException;

import javax.servlet.http.HttpServletRequest;

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
     * 私有化构造函数
     */
    private CommonUtil() {

    }
}
