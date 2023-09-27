package org.itkk.udf.starter.core.trace;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.itkk.udf.starter.core.CoreConstant;
import org.itkk.udf.starter.core.CoreProperties;
import org.itkk.udf.starter.core.CoreUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class TraceContentCachingFilter extends OncePerRequestFilter {

    /**
     * 环境变量
     */
    @Autowired
    private Environment env;

    /**
     * 配置
     */
    @Autowired
    private CoreProperties coreProperties;

    /**
     * internalObjectMapper
     */
    @Autowired
    @Qualifier("internalObjectMapper")
    private ObjectMapper internalObjectMapper;

    /**
     * 追踪通知
     */
    @Autowired(required = false)
    private ITraceAlert iTraceAlert;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获得相关信息
        final long startTime = System.currentTimeMillis();
        final String traceId = loadTraceId(request);
        final String profilesActive = env.getProperty("spring.profiles.active");
        final String url = request.getRequestURI();
        final String method = request.getMethod();
        final String queryString = request.getQueryString();
        final Map<String, String[]> parameterMap = request.getParameterMap();
        //判断是否排除
        final boolean exclude = ArrayUtils.isNotEmpty(coreProperties.getContentExclude()) && Arrays.stream(coreProperties.getContentExclude()).anyMatch(a -> url.contains(a));
        //基础信息放入request作用域中
        request.setAttribute(CoreConstant.REQUEST_TRACE_ID_KEY, traceId);
        //请求放行(替换为缓存body的实现)
        ContentCachingRequestWrapper contentCachingRequestWrapper = null;
        ContentCachingResponseWrapper contentCachingResponseWrapper = null;
        //开关判断
        if (coreProperties.getContentCaching() && !exclude) {
            contentCachingRequestWrapper = new ContentCachingRequestWrapper(request);
            contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
            filterChain.doFilter(contentCachingRequestWrapper, contentCachingResponseWrapper);
        } else {
            filterChain.doFilter(request, response);
        }
        //获得相关信息
        String requestBody = null;
        String responseBody = null;
        if (coreProperties.getContentCaching() && contentCachingRequestWrapper != null && contentCachingResponseWrapper != null) {
            if (StringUtils.isNotBlank(contentCachingRequestWrapper.getContentType())) {
                if (contentCachingRequestWrapper.getContentType().toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE.toLowerCase())) {
                    requestBody = CoreUtil.getPayLoad(contentCachingRequestWrapper.getContentAsByteArray());
                }
            }
            if (StringUtils.isNotBlank(contentCachingResponseWrapper.getContentType())) {
                if (contentCachingResponseWrapper.getContentType().toLowerCase().contains(MediaType.APPLICATION_JSON_VALUE.toLowerCase())) {
                    responseBody = CoreUtil.getPayLoad(contentCachingResponseWrapper.getContentAsByteArray());
                }
            }
        }
        String requestContentType = request.getContentType();
        String responseContentType = response.getContentType();
        int httpStatus = response.getStatus();
        long endTime = System.currentTimeMillis();
        long timeCost = endTime - startTime;
        //设置响应头
        response.setHeader(CoreConstant.REQUEST_PROFILES_ACTIVE_KEY, profilesActive);
        response.setHeader(CoreConstant.REQUEST_TRACE_ID_KEY, traceId);
        response.setHeader(CoreConstant.REQUEST_START_TIME_KEY, Long.valueOf(startTime).toString());
        response.setHeader(CoreConstant.REQUEST_END_TIME_KEY, Long.valueOf(endTime).toString());
        response.setHeader(CoreConstant.REQUEST_TIME_COST_KEY, Long.valueOf(timeCost).toString());
        //回执响应body
        if (coreProperties.getContentCaching() && contentCachingResponseWrapper != null) {
            contentCachingResponseWrapper.copyBodyToResponse();
        }
        //构造日志对象
        TraceDto traceDto = new TraceDto()
                .setProfilesActive(profilesActive)
                .setTraceId(traceId)
                .setUrl(url)
                .setMethod(method)
                .setQueryString(queryString)
                .setRequestBody(requestBody)
                .setResponseBody(responseBody)
                .setRequestContentType(requestContentType)
                .setResponseContentType(responseContentType)
                .setHttpStatus(httpStatus)
                .setStartTime(startTime)
                .setEndTime(endTime)
                .setTimeCost(timeCost);
        //异常通知
        if (coreProperties.getTraceAlert()) {
            if (iTraceAlert != null) {
                if (!exclude) {
                    iTraceAlert.alert(traceDto);
                }
            }
        }
        //输出日志对象
        if (log.isDebugEnabled()) {
            log.debug("{} --> trace log : {}", traceDto.getTraceId(), internalObjectMapper.writeValueAsString(traceDto));
        }
    }

    /**
     * 加载追踪ID
     *
     * @param request request
     * @return String
     */
    public String loadTraceId(HttpServletRequest request) {
        String traceId = null;
        Object traceIdObj = request.getAttribute(CoreConstant.REQUEST_TRACE_ID_KEY);
        if (traceIdObj != null) {
            traceId = (String) traceIdObj;
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = request.getParameter(CoreConstant.REQUEST_TRACE_ID_KEY);
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = request.getParameter(CoreConstant.REQUEST_TRACE_ID_KEY);
        }
        if (StringUtils.isBlank(traceId)) {
            traceId = UUID.randomUUID().toString();
        }
        return traceId;
    }
}
