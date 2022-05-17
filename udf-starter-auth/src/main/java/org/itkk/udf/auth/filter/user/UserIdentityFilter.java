package org.itkk.udf.auth.filter.user;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.auth.Constant;
import org.itkk.udf.auth.IUserAuthenticationService;
import org.itkk.udf.auth.UserType;
import org.itkk.udf.auth.filter.AbstractBaseZuulFilter;
import org.itkk.udf.core.exception.SystemRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * IdentityFilter
 */
@Component
@Slf4j
@ConditionalOnProperty(value = "org.itkk.auth.filter.user.identity.enabled", matchIfMissing = true)
public class UserIdentityFilter extends AbstractBaseZuulFilter {

    /**
     * userAuthenticationService
     */
    @Autowired(required = false)
    private IUserAuthenticationService userAuthenticationService;

    @Override
    public Object run() {
        log.info("UserIdentityFilter.run");
        //判空
        if (userAuthenticationService == null) {
            throw new SystemRuntimeException("You need to implement the IUserAuthenticationService interface");
        }
        //获得accessToken
        String accessToken = this.getAccessToken();
        //校验token
        userAuthenticationService.verification(accessToken);
        //返回
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return this.checkUserType(UserType.USER) && this.checkExclude(UserType.USER);
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return Constant.ORDER_IDENTITY;
    }

}
