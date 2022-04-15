package org.itkk.udf.auth.filter;

import com.netflix.zuul.ZuulFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * PurviewFilter
 */
@Component
@Slf4j
public class PurviewFilter extends ZuulFilter {

    @Override
    public Object run() {
        log.info("PurviewFilter.run");
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        final int order = 10001;
        return order;
    }

}
