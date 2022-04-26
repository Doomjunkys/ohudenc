package org.itkk.udf.auth.component;


import org.itkk.udf.auth.IUserAuthenticationService;
import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;

import java.util.UUID;

/**
 * DefUserAuthenticationService
 */
public class DefUserAuthenticationService implements IUserAuthenticationService {
    @Override
    public UserAuthenticationResult authentication(UserAuthenticationModel model) {
        UserAuthenticationResult userAuthenticationResult = new UserAuthenticationResult();
        userAuthenticationResult.setAccessToken(UUID.randomUUID().toString());
        return userAuthenticationResult;
    }
}
