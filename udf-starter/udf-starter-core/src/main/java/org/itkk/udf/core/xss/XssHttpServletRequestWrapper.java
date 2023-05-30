/**
 * XssHttpServletRequestWrapper.java
 * Created at 2016-09-19
 * Created by wangkang
 * Copyright (C) 2016 itkk.org, All rights reserved.
 */
package org.itkk.udf.core.xss;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 描述 : 跨站请求防范
 *
 * @author wangkang
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
        if (StringUtils.isNotBlank(value)) {
            return HtmlUtils.htmlEscape(value);
        }
        return null;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            return HtmlUtils.htmlEscape(value);
        }
        return null;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                if (StringUtils.isNotBlank(values[i])) {
                    escapseValues[i] = HtmlUtils.htmlEscape(values[i]);
                } else {
                    escapseValues[i] = null;
                }
            }
            return escapseValues;
        }
        return super.getParameterValues(name);
    }

}
