package org.itkk.udf.auth.component;


import org.itkk.udf.auth.IUserAuthenticationService;
import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;

/**
 * DefUserAuthenticationService
 */
public class DefUserAuthenticationService implements IUserAuthenticationService {
    @Override
    public UserAuthenticationResult authentication(UserAuthenticationModel model) {
        return null;
    }
}
