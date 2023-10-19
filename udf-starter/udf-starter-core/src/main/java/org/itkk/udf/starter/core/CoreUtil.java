package org.itkk.udf.starter.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.itkk.udf.starter.core.exception.AuthException;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 工具类
 */
@Slf4j
public class CoreUtil {

    /**
     * 判断是否大写英文字符串(含标点)
     *
     * @param s s
     * @return 是大写英文字符串 true , 不是大写英文字符串 false
     */
    public static boolean isUppercaseEnglish(String s) {
        final String dic = "abcdefghijklmnopqrstuvwsyz";
        boolean rv = true;
        for (char c : s.toCharArray()) {
            //是小写,或者是中文字符,或者是全角标点,则校验不通过
            if (ArrayUtils.indexOf(dic.toCharArray(), c) != -1 || isChineseByScript(c) || isChinesePunctuation(c)) {
                rv = false;
                break;
            }
        }
        return rv;
    }

    /**
     * 是否为中文或者中文标点
     *
     * @param s s
     * @return boolean
     */
    public static boolean isChinese(String s) {
        //定义返回值
        boolean rv = false;
        //遍历
        for (char c : s.toCharArray()) {
            if (isChineseByScript(c) || isChinesePunctuation(c)) {
                rv = true;
                break;
            }
        }
        //返回
        return rv;
    }

    /**
     * 判断是否为中文
     *
     * @param c c
     * @return 是中文 true , 不是中文 false
     */
    private static boolean isChineseByScript(char c) {
        Character.UnicodeScript sc = Character.UnicodeScript.of(c);
        return sc == Character.UnicodeScript.HAN;
    }

    /**
     * 判断中文标点符号
     *
     * @param s s
     * @return 包含中文标点 true , 不包含中文标点 false
     */
    public static boolean isChinesePunctuation(String s) {
        boolean rv = false;
        for (char c : s.toCharArray()) {
            if (isChinesePunctuation(c)) {
                rv = true;
                break;
            }
        }
        return rv;
    }

    /**
     * 判断中文标点符号
     *
     * @param c c
     * @return 是中文标点 true , 不是中文标点 false
     */
    private static boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS;
    }

    /**
     * 校验(如果有错误,返回第一条错误)
     *
     * @param object object
     * @param <T>
     */
    public static <T> void valid(final T object) {
        Set<ConstraintViolation<T>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(object);
        if (!violations.isEmpty()) {
            throw new ParameterValidException(violations.iterator().next().getMessage());
        }
    }

    /**
     * 生成sign
     *
     * @param appName appName
     * @param secret  secret
     * @return String
     */
    public static String getSign(String appName, String secret) {
        //判空
        if (StringUtils.isBlank(appName)) {
            throw new AuthException("appName不能为空");
        }
        if (StringUtils.isBlank(secret)) {
            throw new AuthException("secret不能为空");
        }
        // 构造
        final String split = "_";
        final String dataFormatPattern = "yyyyMMddHH";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataFormatPattern);
        StringBuilder key = new StringBuilder()
                .append(appName)
                .append(split)
                .append(secret)
                .append(split)
                .append(simpleDateFormat.format(new Date()));
        // 生成 & 返回
        return DigestUtils.md5Hex(key.toString());
    }

    /**
     * 检查sign
     *
     * @param appName appName
     * @param sign    sign
     * @param secret  secret
     */
    public static void checkSign(String appName, String sign, String secret) {
        //判空
        if (StringUtils.isBlank(appName)) {
            throw new AuthException("appName不能为空");
        }
        if (StringUtils.isBlank(sign)) {
            throw new AuthException("sign不能为空");
        }
        if (StringUtils.isBlank(secret)) {
            throw new AuthException("secret不能为空");
        }
        // 生成比对key
        final String split = "_";
        final String dataFormatPattern = "yyyyMMddHH";
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dataFormatPattern);
        StringBuilder key = new StringBuilder()
                .append(appName)
                .append(split)
                .append(secret)
                .append(split)
                .append(simpleDateFormat.format(new Date()));
        String md5Key = DigestUtils.md5Hex(key.toString());
        //日志输出
        log.info("{} checkSign --> sign : {} , md5Key : {} , key ; {} ", CoreUtil.getTraceId(), md5Key, sign, key.toString());
        //比对
        if (!md5Key.equals(sign)) {
            throw new AuthException("签名校验错误");
        }
    }

    /**
     * 属性拷贝(忽略为空的属性)
     *
     * @param src    src
     * @param target target
     */
    public static void copyPropertiesIgnoreNull(Object src, Object target) {
        BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
    }

    /**
     * 属性拷贝(忽略为空的属性)
     *
     * @param source source
     * @return String[]
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    /**
     * dto转换(原始类型-->json-->目标类型)
     *
     * @param obj obj
     * @param t   t
     * @param <T> <T>
     * @return <T>
     */
    public static <T> T convertDto(Object obj, Class<T> t) {
        //获得objectMapper
        ObjectMapper objectMapper = CoreConstant.applicationContext.getBean("internalObjectMapper", ObjectMapper.class);
        //定义返回值
        T dto = null;
        try {
            //判空
            if (obj != null) {
                //转换成jsonStr
                String jsonStr = objectMapper.writeValueAsString(obj);
                //判空
                if (StringUtils.isNotBlank(jsonStr)) {
                    //转换成实体对象
                    dto = objectMapper.readValue(jsonStr, t);
                }
            }
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
        //返回
        return dto;
    }

    /**
     * dtoList转换(原始类型-->json-->目标类型)
     *
     * @param obj           obj
     * @param typeReference typeReference
     * @param <T>           <T>
     * @return <T>
     */
    public static <T> List<T> convertDtoList(Object obj, TypeReference<List<T>> typeReference) {
        //获得objectMapper
        ObjectMapper objectMapper = CoreConstant.applicationContext.getBean("internalObjectMapper", ObjectMapper.class);
        //定义返回值
        List<T> dtoLost = null;
        try {
            //判空
            if (obj != null) {
                //转换成jsonStr
                String jsonStr = objectMapper.writeValueAsString(obj);
                //判空
                if (StringUtils.isNotBlank(jsonStr)) {
                    //转换成实体对象
                    dtoLost = objectMapper.readValue(jsonStr, typeReference);
                }
            }
        } catch (Exception e) {
            throw new SystemRuntimeException(e);
        }
        //返回
        return dtoLost;
    }

    /**
     * 加载追踪ID
     *
     * @return String
     */
    public static String getTraceId() {
        try {
            Object traceIdObj = RequestContextHolder.getRequestAttributes().getAttribute(CoreConstant.REQUEST_TRACE_ID_KEY, RequestAttributes.SCOPE_REQUEST);
            if (traceIdObj != null) {
                return (String) traceIdObj;
            }
        } catch (Exception e) {
            log.debug("get traceId fail");
        }
        //生成traceId
        String traceId = UUID.randomUUID().toString();
        //放入作用域
        try {
            RequestContextHolder.getRequestAttributes().setAttribute(CoreConstant.REQUEST_TRACE_ID_KEY, traceId, RequestAttributes.SCOPE_REQUEST);
        } catch (Exception e) {
            log.debug("set traceId fail");
        }
        //返回
        return traceId;
    }

    /**
     * 修复CORS问题
     *
     * @param response response
     */
    public static void fixCors(HttpServletResponse response, HttpServletRequest req, CoreProperties coreProperties) {
        final String originHeaderName = "Origin";
        final String headerAccessControlAllowOrigin = "Access-Control-Allow-Origin";
        final String headerAccessControlAllowMethods = "Access-Control-Allow-Methods";
        final String headerAccessControlAllowHeaders = "Access-Control-Allow-Headers";
        final String headerValue = "*";
        if (StringUtils.isBlank(response.getHeader(headerAccessControlAllowOrigin))) {
            String originHeader = req.getHeader(originHeaderName);
            if (StringUtils.isNotBlank(originHeader)) {
                String[] corsAllowedOrigins = coreProperties.getCorsAllowedOrigins().split(",");
                Set<String> allowedOrigins = new HashSet<>(Arrays.asList(corsAllowedOrigins));
                if (allowedOrigins.contains(originHeader)) {
                    response.setHeader(headerAccessControlAllowOrigin, originHeader);
                }
            } else {
                response.setHeader(headerAccessControlAllowOrigin, headerValue);
            }
        }
        if (StringUtils.isBlank(response.getHeader(headerAccessControlAllowMethods))) {
            response.setHeader(headerAccessControlAllowMethods, headerValue);
        }
        if (StringUtils.isBlank(response.getHeader(headerAccessControlAllowHeaders))) {
            response.setHeader(headerAccessControlAllowHeaders, headerValue);
        }
    }

    /**
     * 读取字节信息
     *
     * @param buf 字节流
     * @return String
     */
    public static String getPayLoad(byte[] buf) {
        try {
            return new String(buf, 0, buf.length, CoreConstant.CHARACTER_SET);
        } catch (UnsupportedEncodingException e) {
            throw new SystemRuntimeException(e);
        }
    }

    /**
     * 读取流信息
     *
     * @param in in
     * @return String
     * @throws IOException IOException
     */
    public static String getData(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new InputStreamReader(in, CoreConstant.CHARACTER_SET));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return sb.toString();
    }

    private static final double DEFAULT_DELTA = 0.000001; //默认比较精度

    //比较2个double值是否相等（默认精度）
    public static boolean considerEqual(double v1, double v2) {
        return considerEqual(v1, v2, DEFAULT_DELTA);
    }

    //比较2个double值是否相等（指定精度）
    public static boolean considerEqual(double v1, double v2, double delta) {
        return Double.compare(v1, v2) == 0 || considerZero(v1 - v2, delta);
    }

    //判断指定double是否为0（默认精度）
    public static boolean considerZero(double value) {
        return considerZero(value, DEFAULT_DELTA);
    }

    //判断指定double是否为0（指定精度）
    public static boolean considerZero(double value, double delta) {
        return Math.abs(value) <= delta;
    }

    /**
     * 获得HttpServletRequest
     *
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getRequest();
        }
        return null;
    }

    /**
     * 获得HttpServletResponse
     *
     * @return HttpServletResponse
     */
    public static HttpServletResponse getHttpServletResponse() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
            return servletRequestAttributes.getResponse();
        }
        return null;
    }

    /**
     * 获得参数(先从问号后面取,如果为空,再从header里取,如果为空,从作用域中取,如果都为空,则返回null)
     *
     * @param request       request
     * @param parameterName parameterName
     * @return String
     */
    public static String getParameter(HttpServletRequest request, String parameterName) {
        //获得token
        String token = request.getParameter(parameterName);
        if (StringUtils.isBlank(token)) {
            token = request.getHeader(parameterName);
            if (StringUtils.isBlank(token)) {
                Object tokenObj = request.getAttribute(parameterName);
                if (tokenObj != null) {
                    token = tokenObj.toString();
                }
            }
        }
        //返回
        return token;
    }


    /**
     * 私有化构造函数
     */
    private CoreUtil() {

    }

}
