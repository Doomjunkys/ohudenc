package org.itkk.udf.rms;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.itkk.udf.core.exception.AuthException;
import org.itkk.udf.rms.meta.ApplicationMeta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

/**
 * RmsFilter
 */
public class RmsFilter extends ZuulFilter {

    /**
     * 描述 : 应用名称
     */
    @Value("${spring.application.name}")
    private String springApplicationName;

    /**
     * 描述 : 配置
     */
    @Autowired
    private RmsProperties rmsProperties;

    @Override
    public Object run() {
        //获得request
        RequestContext ctx = RequestContext.getCurrentContext();
        //获得应用元数据
        ApplicationMeta applicationMeta = rmsProperties.getApplication().get(springApplicationName);
        //判空
        if (applicationMeta == null) {
            throw new AuthException("unrecognized systemTag:" + springApplicationName);
        }
        //获得secret
        String secret = applicationMeta.getSecret();
        //计算sign
        String sign = Constant.sign(springApplicationName, secret);
        //设置头
        ctx.addZuulRequestHeader(Constant.HEADER_RMS_APPLICATION_NAME_CODE, springApplicationName);
        ctx.addZuulRequestHeader(Constant.HEADER_RMS_SIGN_CODE, sign);
        //执行
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }
}
