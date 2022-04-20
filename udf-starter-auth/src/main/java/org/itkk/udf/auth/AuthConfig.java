package org.itkk.udf.auth;

import org.itkk.udf.auth.component.DefUserAuthenticationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AuthConfig
 */
@Configuration
public class AuthConfig {

    /**
     * defUserAuthenticationService
     *
     * @return IUserAuthenticationService
     */
    @Bean
    @ConditionalOnMissingBean(IUserAuthenticationService.class)
    public IUserAuthenticationService defUserAuthenticationService() {
        return new DefUserAuthenticationService();
    }

}
