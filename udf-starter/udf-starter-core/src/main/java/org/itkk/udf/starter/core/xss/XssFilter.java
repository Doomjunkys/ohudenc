package org.itkk.udf.starter.core.xss;

import org.itkk.udf.starter.core.CoreConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class XssFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //排除
        if (request.getRequestURI().contains(CoreConstant.OPS_PATH_ROOT)) {
            filterChain.doFilter(request, response);
        } else {
            XssHttpServletRequestWrapper xssRequestWrapper = new XssHttpServletRequestWrapper(request);
            filterChain.doFilter(request, response);
        }
    }
}
