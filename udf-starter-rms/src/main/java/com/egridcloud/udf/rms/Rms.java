/**
 * Rms.java
 * Created at 2017-04-24
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.egridcloud.udf.core.RestResponse;
import com.egridcloud.udf.core.exception.SystemRuntimeException;
import com.egridcloud.udf.rms.mate.AuthMate;
import com.egridcloud.udf.rms.mate.PathMate;

/**
 * 描述 : 远程服务
 *
 * @author Administrator
 *
 */
@Component
public class Rms {

  /**
   * 描述 : 日志
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(Rms.class);

  /**
   * 描述 : 应用名称
   */
  @Value("${spring.application.name}")
  private String springApplicationName;

  /**
   * 描述 : restTemplate
   */
  @Autowired
  private RestTemplate restTemplate;

  /**
   * 描述 : 配置
   */
  @Autowired
  private RmsProperties rmsProperties;

  /**
   * 描述 : 远程服务调用
   *
   * @param serviceCode 服务代码
   * @param input 输入参数
   * @param uriParam uri参数
   * @param uriVariables rest参数
   * @param <I> 输入类型
   * @param <O> 输出类型
   * @return 服务结果
   */
  public <I, O> RestResponse<O> call(String serviceCode, I input, String uriParam,
      Object... uriVariables) {
    //构建请求路径
    String path = getRmsUrl(serviceCode);
    //获得请求方法
    String method = getRmsMethod(serviceCode);
    //拼装路径参数
    if (StringUtils.isNotBlank(uriParam)) {
      path += uriParam;
    }
    //构建请求头
    HttpHeaders httpHeaders = buildSystemTagHeaders(serviceCode);
    //构建请求消息体
    HttpEntity<I> requestEntity = new HttpEntity<>(input, httpHeaders);
    //请求并且返回
    LOGGER.info("rms url : {} , method : {} ", path, method);
    return restTemplate.exchange(path, HttpMethod.resolve(method), requestEntity,
        new ParameterizedTypeReference<RestResponse<O>>() {
        }, uriVariables).getBody();
  }

  /**
   * 描述 : 构建请求头
   *
   * @param serviceCode 服务代码
   * @return 请求头
   */
  private HttpHeaders buildSystemTagHeaders(String serviceCode) {
    String secret = rmsProperties.getAuthorization().get(springApplicationName).getSecret();
    HttpHeaders headers = new HttpHeaders();
    headers.add(Constant.HEADER_RMS_APPLICATION_NAME_CODE, springApplicationName);
    headers.add(Constant.HEADER_RMS_SIGN_CODE, Constant.sign(springApplicationName, secret));
    headers.add(Constant.HEADER_SERVICE_CODE_CODE, serviceCode);
    return headers;
  }

  /**
   * 描述 : 获得请求方法
   *
   * @param serviceCode 服务代码
   * @return 请求方法
   */
  private String getRmsMethod(String serviceCode) {
    return rmsProperties.getService().get(serviceCode).getMethod();
  }

  /**
   * 描述 : 构造url;
   *
   * @param serviceCode 服务代码
   * @return url
   */
  private String getRmsUrl(String serviceCode) {
    //获得认证元数据
    AuthMate authMate = rmsProperties.getAuthorization().get(springApplicationName);
    //获取路径元数据
    PathMate pathMate = rmsProperties.getService().get(springApplicationName);
    //判断路径访问权限
    if (authMate.getPurview() != null) {
      String[] purviewArray = authMate.getPurview().split(",");
      List<String> purviewList = Arrays.asList(purviewArray);
      if (purviewList.contains(Constant.PURVIEW_DISABLED)) {
        throw new SystemRuntimeException(springApplicationName + " is disabled");
      }
      if (!purviewList.contains(Constant.PURVIEW_ALL) && !purviewList.contains(serviceCode)) {
        throw new SystemRuntimeException(serviceCode + " no access");
      }
    } else {
      throw new SystemRuntimeException(springApplicationName + " purview is null");
    }
    StringBuilder url = new StringBuilder(Constant.HTTP);
    url.append(rmsProperties.getService().get(pathMate.getApplicationName()));
    url.append(pathMate.getUri());
    return url.toString();
  }

}
