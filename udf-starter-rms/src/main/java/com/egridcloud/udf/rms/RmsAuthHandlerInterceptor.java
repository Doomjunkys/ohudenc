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
import com.egridcloud.udf.rms.mate.ApplicationMate;
import com.egridcloud.udf.rms.mate.ServiceMate;

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
    LOGGER.info("rmsApplicationName:{},rmsSign:{},rmsServiceCode:{},url:{},method:{}", rmsApplicationName, rmsSign,
        rmsServiceCode, url, method);
    //判断是否缺少认证信息
    if (StringUtils.isBlank(rmsApplicationName) || StringUtils.isBlank(rmsSign)
        || StringUtils.isBlank(rmsServiceCode)) {
      throw new AuthException("missing required authentication parameters (rmsApplicationName , rmsSign)");
    }
    //判断环境(开发环境无需校验)
    if (!DEV_PROFILES.equals(env.getProperty("spring.profiles.active"))) {
      //判断systemTag是否有效
      if (this.rmsProperties.getApplication().containsKey(rmsApplicationName)) {
        //获得secret
        String secret = this.rmsProperties.getApplication().get(rmsApplicationName).getSecret();
        //计算sign
        String sign = Constant.sign(rmsApplicationName, secret);
        //比较sign
        if (!rmsSign.equals(sign)) {
          throw new AuthException("sign Validation failed");
        }
        //比较是否有调用接口的权限
        ApplicationMate applicationMate = rmsProperties.getApplication().get(rmsApplicationName);
        //判断是否有调用所有服务的权限
        if (!applicationMate.getAll()) {
          //判断是否禁止调用所有服务权限
          if (applicationMate.getDisabled()) {
            throw new PermissionException(rmsApplicationName + " is disabled");
          }
          //判断是否有调用该服务的权限
          if (applicationMate.getPurview().indexOf(rmsServiceCode) != -1) {
            //比较接口编号和服务设定的有效性
            if (rmsProperties.getService().containsKey(rmsServiceCode)) {
              //获得服务元数据
              ServiceMate serviceMate = rmsProperties.getService().get(rmsServiceCode);
              //比较url和method
              if (!serviceMate.getUri().equals(url) || !serviceMate.getMethod().equals(method)) {
                throw new PermissionException("url and method verification error");
              }
            } else {
              throw new PermissionException("service code not exist");
            }
          } else {
            throw new PermissionException("no access to this servoceCode : " + rmsServiceCode);
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
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    LOGGER.debug("SystemTagAuthHandlerInterceptor.afterCompletion");
  }

}
