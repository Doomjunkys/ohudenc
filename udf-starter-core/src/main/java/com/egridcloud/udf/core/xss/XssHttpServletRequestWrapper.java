/**
 * XssHttpServletRequestWrapper.java
 * Created at 2016-09-19
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package com.egridcloud.udf.core.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.web.util.HtmlUtils;

/**
 * 描述 : 跨站请求防范
 *
 * @author wangkang
 *
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

  /**
   * 描述 : 构造函数
   *
   * @param request 请求对象
   */
  public XssHttpServletRequestWrapper(HttpServletRequest request) {
    super(request);
  }

  @Override
  public String getHeader(String name) {
    String value = super.getHeader(name);
    return HtmlUtils.htmlEscape(value);
  }

  @Override
  public String getParameter(String name) {
    String value = super.getParameter(name);
    return HtmlUtils.htmlEscape(value);
  }

  @Override
  public String[] getParameterValues(String name) {
    String[] values = super.getParameterValues(name);
    if (values != null) {
      int length = values.length;
      String[] escapseValues = new String[length];
      for (int i = 0; i < length; i++) {
        escapseValues[i] = HtmlUtils.htmlEscape(values[i]);
      }
      return escapseValues;
    }
    return super.getParameterValues(name);
  }

}
