package org.itkk.udf.auth.web;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.auth.IUserAuthenticationService;
import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserAuthenticationController
 */
@RestController
@Slf4j
public class UserAuthenticationController implements IUserAuthenticationController {
    /**
     * userAuthenticationService
     */
    @Autowired(required = false)
    private IUserAuthenticationService userAuthenticationService;

    @Override
    public RestResponse<UserAuthenticationResult> authentication(@RequestBody UserAuthenticationModel model) {
        if (userAuthenticationService == null) {
            throw new SystemRuntimeException("You need to implement the IUserAuthenticationService interface");
        }
        return new RestResponse<>(new UserAuthenticationResult(userAuthenticationService.authentication(model.getAccount(), model.getPassword(), model.isSingleton(), model.isForced())));
    }
}
