package org.itkk.udf.core.exception.handle;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.core.ApplicationConfig;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.ErrorResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ErrorController
 */
@ApiIgnore
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
@Slf4j
public class ExceptionController extends AbstractErrorController {

    /**
     * exception
     */
    private static final String KEY_EXCEPTION = "exception";

    /**
     * message
     */
    private static final String KEY_MESSAGE = "message";


    /**
     * errorProperties
     */
    private final ErrorProperties errorProperties;

    /**
     * 描述 : 系统配置
     */
    @Autowired
    private ApplicationConfig applicationConfig;

    /**
     * Create a new {@link ExceptionController} instance.
     *
     * @param errorAttributes the error attributes
     * @param errorProperties configuration properties
     */
    public ExceptionController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        this(errorAttributes, errorProperties, new ArrayList<>());
    }

    /**
     * Create a new {@link ExceptionController} instance.
     *
     * @param errorAttributes    the error attributes
     * @param errorProperties    configuration properties
     * @param errorViewResolvers error view resolvers
     */
    public ExceptionController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorViewResolvers);
        Assert.notNull(errorProperties, "ErrorProperties must not be null");
        this.errorProperties = errorProperties;
    }

    @Override
    public String getErrorPath() {
        return this.errorProperties.getPath();
    }

    /**
     * html错误
     *
     * @param request  request
     * @param response response
     * @return ModelAndView
     */
    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        HttpStatus status = getStatus(request);
        Map<String, Object> model = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML));
        RestResponse<String> restResponse = this.getRestResponse(request, status, model);
        model.put("restResponse", restResponse);
        model.put(KEY_EXCEPTION, restResponse.getError().getType());
        model.put(KEY_MESSAGE, restResponse.getError().getMessage());
        response.setStatus(status.value());
        ModelAndView modelAndView = resolveErrorView(request, response, status, model);
        return (modelAndView == null ? new ModelAndView("error", model) : modelAndView);
    }

    /**
     * json错误
     *
     * @param request request
     * @return ResponseEntity
     */
    @RequestMapping
    @ResponseBody
    public ResponseEntity<RestResponse<String>> error(HttpServletRequest request) {
        Map<String, Object> body = getErrorAttributes(request, isIncludeStackTrace(request, MediaType.ALL));
        HttpStatus status = getStatus(request);
        return new ResponseEntity<>(getRestResponse(request, status, body), status);
    }

    /**
     * 获得RestResponse
     *
     * @param request request
     * @param status  status
     * @param body    body
     * @return RestResponse
     */
    private RestResponse<String> getRestResponse(HttpServletRequest request, HttpStatus status, Map<String, Object> body) { //NOSONAR
        log.info(body.toString());
        ErrorResult errorResult = new ErrorResult();
        if (status == HttpStatus.NOT_FOUND) { //404处理
            errorResult.setType(NoHandlerFoundException.class.getName());
            errorResult.setMessage(body.get("path").toString());
        } else { //非404处理
            Object object = request.getAttribute("javax.servlet.error.exception");
            if (object != null && object instanceof Exception) { //上下文中能拿到异常的情况
                Exception exception = (Exception) object;
                //ZuulException异常特殊处理,去除ZuulException的包裹 (不用instanceof的原因是不想因为这里的判断而引入zuul的依赖在core包中)
                if (exception.getClass().getName().equals("com.netflix.zuul.exception.ZuulException")) { //NOSONAR
                    errorResult = ExceptionHandle.buildError(applicationConfig, exception.getCause());
                } else {
                    errorResult = ExceptionHandle.buildError(applicationConfig, exception);
                }
            } else { //上下文中拿不到异常的情况
                errorResult.setType(body.containsKey(KEY_EXCEPTION) ? body.get(KEY_EXCEPTION).toString() : "unknow exception");
                errorResult.setMessage(body.containsKey(KEY_MESSAGE) ? body.get(KEY_MESSAGE).toString() : "unknow message");
            }
        }
        errorResult.setDate(new Date());
        return new RestResponse<>(status, errorResult);
    }

    /**
     * Determine if the stacktrace attribute should be included.
     *
     * @param request  the source request
     * @param produces the media type produced (or {@code MediaType.ALL})
     * @return if the stacktrace attribute should be included
     */
    protected boolean isIncludeStackTrace(HttpServletRequest request, MediaType produces) { //NOSONAR
        ErrorProperties.IncludeStacktrace include = getErrorProperties().getIncludeStacktrace();
        return include == ErrorProperties.IncludeStacktrace.ALWAYS || include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM && getTraceParameter(request);
    }

    /**
     * Provide access to the error properties.
     *
     * @return the error properties
     */
    protected ErrorProperties getErrorProperties() {
        return this.errorProperties;
    }

}
