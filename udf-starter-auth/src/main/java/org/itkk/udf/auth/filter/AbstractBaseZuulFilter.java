package org.itkk.udf.auth.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.auth.AuthProperties;
import org.itkk.udf.auth.Constant;
import org.itkk.udf.auth.UserType;
import org.itkk.udf.auth.meta.ExcludeServiceMeta;
import org.itkk.udf.core.exception.AuthException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.netflix.zuul.context.RequestContext.getCurrentContext;

/**
 * AbstractBaseZuulFilter
 */
public abstract class AbstractBaseZuulFilter extends ZuulFilter {

    /**
     * authProperties
     */
    @Autowired
    private AuthProperties authProperties;

    /**
     * 判断排除
     *
     * @param targetUserType 目标用户类型
     * @return 是否匹配
     */
    public boolean checkExclude(UserType targetUserType) {
        //获得request
        RequestContext ctx = getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        //获得url和method
        String url = request.getRequestURI();
        String method = request.getMethod();
        //获得排除列表
        Map<String, ExcludeServiceMeta> map = new HashMap<>();
        if (targetUserType.equals(UserType.USER)) {
            map = authProperties.getUserExcludeService();
        } else if (targetUserType.equals(UserType.CLIENT)) {
            map = authProperties.getClientExcludeServiceMeta();
        }
        //判空 & 遍历 & 匹配排除规则
        if (MapUtils.isNotEmpty(map)) {
            Iterator<String> keys = map.keySet().iterator();
            while (keys.hasNext()) {
                ExcludeServiceMeta item = map.get(keys.next());
                if (item.getUrl().equals(url) && item.getMethod().equals(method)) {
                    return false;
                }
            }
        }
        //返回
        return true;
    }

    /**
     * 判断用户类型
     * s
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
