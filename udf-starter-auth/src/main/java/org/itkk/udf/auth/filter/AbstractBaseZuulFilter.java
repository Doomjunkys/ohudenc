package org.itkk.udf.auth.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.auth.Constant;
import org.itkk.udf.auth.UserType;
import org.itkk.udf.core.exception.AuthException;

import javax.servlet.http.HttpServletRequest;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * AbstractBaseZuulFilter
 */
public abstract class AbstractBaseZuulFilter extends ZuulFilter {
    /**
     * 判断用户类型
     *
     * @param targetUserType 目标用户类型
     * @return 是否匹配
     */
    public boolean checkUserType(UserType targetUserType) {
        //获得request
        RequestContext ctx = getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //获得用户类型
        String userType = request.getHeader(Constant.HEADER_USER_TYPE);
        if (StringUtils.isBlank(userType)) {
            userType = request.getParameter(Constant.HEADER_USER_TYPE);
        }
        //判空
        if (StringUtils.isBlank(userType)) {
            throw new AuthException("must be set userType");
        }
        //匹配
        UserType userTypeEnum = UserType.resolve(userType);
        if (userTypeEnum == null) {
            throw new AuthException("unknow userType");
        }
        //判断是否拦截 & 返回
        return userTypeEnum.equals(targetUserType);
    }
}
