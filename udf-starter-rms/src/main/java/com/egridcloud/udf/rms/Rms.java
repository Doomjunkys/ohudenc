/**
 * Rms.java
 * Created at 2017-04-24
 * Created by Administrator
 * Copyright (C) 2016 egridcloud.com, All rights reserved.
 */
package com.egridcloud.udf.rms;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.egridcloud.udf.core.exception.PermissionException;
import com.egridcloud.udf.rms.mate.ApplicationMate;
import com.egridcloud.udf.rms.mate.ServiceMate;

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
   * @param responseType 返回类型
   * @param uriVariables rest参数
   * @param <I> 输入类型
   * @param <O> 输出类型
   * @return 服务结果
   */
  public <I, O> ResponseEntity<O> call(String serviceCode, I input, String uriParam,
      ParameterizedTypeReference<O> responseType, Map<String, ?> uriVariables) {
    //客户端权限验证
    verification(serviceCode);
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
    return restTemplate.exchange(path, HttpMethod.resolve(method), requestEntity, responseType,
        uriVariables != null ? uriVariables : new HashMap<String, String>());
  }

  /**
   * 描述 : 构建请求头
   *
   * @param serviceCode 服务代码
   * @return 请求头
   */
  private HttpHeaders buildSystemTagHeaders(String serviceCode) {
    String secret = rmsProperties.getApplication().get(springApplicationName).getSecret();
    HttpHeaders headers = new HttpHeaders();
    headers.add(Constant.HEADER_RMS_APPLICATION_NAME_CODE, springApplicationName);
    headers.add(Constant.HEADER_RMS_SIGN_CODE, Constant.sign(springApplicationName, secret));
    headers.add(Constant.HEADER_SERVICE_CODE_CODE, serviceCode);
    return headers;
  }

  /**
   * 描述 : 客户端验证
   *
   * @param serviceCode 服务代码
   */
  private void verification(String serviceCode) {
    ApplicationMate applicationMate = rmsProperties.getApplication().get(springApplicationName);
    if (!applicationMate.getAll()) {
      if (applicationMate.getDisabled()) {
        throw new PermissionException(springApplicationName + " is disabled");
      }
      if (applicationMate.getPurview().indexOf(serviceCode) == -1) {
        throw new PermissionException("no access to this servoceCode : " + serviceCode);
      }
    }
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
    //获取服务元数据
    ServiceMate serviceMate = rmsProperties.getService().get(serviceCode);
    //构建请求路径
    StringBuilder url =
        new StringBuilder(serviceMate.getIsHttps() ? Constant.HTTPS : Constant.HTTP);
    url.append(rmsProperties.getApplication().get(serviceMate.getOwner()).getServiceId());
    url.append(serviceMate.getUri());
    return url.toString();
  }

}
