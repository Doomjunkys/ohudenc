package org.itkk.udf.auth.web;

import org.itkk.udf.auth.IUserAuthenticationService;
import org.itkk.udf.auth.domain.UserAuthenticationModel;
import org.itkk.udf.auth.domain.UserAuthenticationResult;
import org.itkk.udf.core.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * UserAuthenticationController
 */
@RestController
public class UserAuthenticationController implements IUserAuthenticationController {
    /**
     * userAuthenticationService
     */
    @Autowired
    private IUserAuthenticationService userAuthenticationService;

    @Override
    public RestResponse<UserAuthenticationResult> authentication(@RequestBody UserAuthenticationModel model) {
        return new RestResponse<>(userAuthenticationService.authentication(model));
    }
}
