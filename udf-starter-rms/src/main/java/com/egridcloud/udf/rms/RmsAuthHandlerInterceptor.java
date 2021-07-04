/**
 * SystemTagAuthHandlerInterceptor.java
 * Created at 2016-11-16
 * Created by wangkang
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import com.egridcloud.udf.core.exception.AuthException;
import com.egridcloud.udf.core.exception.PermissionException;
import com.egridcloud.udf.rms.mate.AuthMate;
import com.egridcloud.udf.rms.mate.PathMate;

/**
 * 描述 : SystemTagAuthHandlerInterceptor
 *
 * @author wangkang
 *
 */
public class RmsAuthHandlerInterceptor implements HandlerInterceptor {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(RmsAuthHandlerInterceptor.class);

  /**
   * 描述 : 环境标识
   */
  private static final String DEV_PROFILES = "dev";

  /**
   * 描述 : 配置
   */
  @Autowired
  private RmsProperties rmsProperties;

  /**
   * 描述 : 环境变量
   */
  @Autowired
  private Environment env;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, //NOSONAR
      Object handler) {
    //获取认证信息
    String rmsApplicationName = request.getHeader(Constant.HEADER_RMS_APPLICATION_NAME_CODE);
    String rmsSign = request.getHeader(Constant.HEADER_RMS_SIGN_CODE);
    String rmsServiceCode = request.getHeader(Constant.HEADER_SERVICE_CODE_CODE);
    String url = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString();
    String method = request.getMethod();
    LOGGER.info("rmsApplicationName:{},rmsSign:{},rmsServiceCode:{},url:{},method:{}",
        rmsApplicationName, rmsSign, rmsServiceCode, url, method);
    //判断是否缺少认证信息
    if (StringUtils.isBlank(rmsApplicationName) || StringUtils.isBlank(rmsSign)
        || StringUtils.isBlank(rmsServiceCode)) {
      throw new AuthException(
          "missing required authentication parameters (rmsApplicationName , rmsSign)");
    }
    //判断环境(开发环境无需校验)
    if (!DEV_PROFILES.equals(env.getProperty("spring.profiles.active"))) {
      //判断systemTag是否有效
      if (this.rmsProperties.getAuthorization().containsKey(rmsApplicationName)) {
        //获得secret
        String secret = this.rmsProperties.getAuthorization().get(rmsApplicationName).getSecret();
        //计算sign
        String sign = Constant.sign(rmsApplicationName, secret);
        //比较sign
        if (!rmsSign.equals(sign)) {
          throw new AuthException("sign Validation failed");
        }
        //比较接口编号的有效性
        if (rmsProperties.getService().containsKey(rmsServiceCode)) {
          //获得服务元数据
          PathMate pathMate = rmsProperties.getService().get(rmsServiceCode);
          //比较url和method
          if (!pathMate.getUri().equals(url) || !pathMate.getMethod().equals(method)) {
            throw new AuthException("url and method verification error");
          }
        } else {
          throw new AuthException("service code not exist");
        }
        //比较是否有调用接口的权限
        AuthMate authMate = rmsProperties.getAuthorization().get(rmsApplicationName);
        if (authMate.getPurview().indexOf(Constant.PURVIEW_ALL) == -1) {
          if (authMate.getPurview().indexOf(Constant.PURVIEW_DISABLED) != -1 //NOSONAR
              || authMate.getPurview().indexOf(rmsServiceCode) == -1) {
            throw new PermissionException(
                "no access to this url : " + url + ", method : " + method);
          }
        }
      } else {
        throw new AuthException("unrecognized systemTag:" + rmsApplicationName);
      }
    }
    //返回
    return true;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {
    LOGGER.debug("SystemTagAuthHandlerInterceptor.postHandle");
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    LOGGER.debug("SystemTagAuthHandlerInterceptor.afterCompletion");
  }

}
