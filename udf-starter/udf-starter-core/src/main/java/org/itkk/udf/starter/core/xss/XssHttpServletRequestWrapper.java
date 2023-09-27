package org.itkk.udf.starter.core.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 对request做XSS过滤处理
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        return XssUtil.xssEncode(super.getHeader(name));
    }

    @Override
    public String getQueryString() {
        return XssUtil.xssEncode(super.getQueryString());
    }

    @Override
    public String getParameter(String name) {
        return XssUtil.xssEncode(super.getParameter(name));
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values != null) {
            int length = values.length;
            String[] escapseValues = new String[length];
            for (int i = 0; i < length; i++) {
                escapseValues[i] = XssUtil.xssEncode(values[i]);
            }
            return escapseValues;
        }
        return values;
    }
}
