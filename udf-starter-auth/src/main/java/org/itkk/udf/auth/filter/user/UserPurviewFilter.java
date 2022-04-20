package org.itkk.udf.auth.filter.user;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.auth.Constant;
import org.itkk.udf.auth.UserType;
import org.itkk.udf.auth.filter.AbstractBaseZuulFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * PurviewFilter
 */
@Component
@Slf4j
@ConditionalOnProperty(value = "org.itkk.auth.filter.user.purview.enabled", matchIfMissing = true)
public class UserPurviewFilter extends AbstractBaseZuulFilter {

    @Override
    public Object run() {
        log.info("UserPurviewFilter.run");
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
        return Constant.ORDER_PURVIEW;
    }

}
