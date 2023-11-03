package org.itkk.udf.api.common.service;

import org.itkk.udf.api.common.dto.UserDto;

public interface IUserService {

    /**
     * 获得用户信息
     *
     * @param userId userId
     * @return UserDto
     */
    UserDto info(String userId);

}
