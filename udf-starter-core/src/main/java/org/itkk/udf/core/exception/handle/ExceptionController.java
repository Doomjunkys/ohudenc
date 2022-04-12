package org.itkk.udf.core.exception.handle;

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
import java.util.*;

/**
 * ErrorController
 */
@ApiIgnore
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ExceptionController extends AbstractErrorController {

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
        Map<String, Object> model = Collections.unmodifiableMap(getErrorAttributes(request, isIncludeStackTrace(request, MediaType.TEXT_HTML)));
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
        String path = body.containsKey("path") ? body.get("path").toString() : "unknow path";
        HttpStatus status = getStatus(request);
        ErrorResult errorResult = new ErrorResult();
        errorResult.setType(NoHandlerFoundException.class.getName());
        errorResult.setMessage("No handler found for " + request.getMethod() + " --> " + path);
        errorResult.setDate(new Date());
        return new ResponseEntity<>(new RestResponse<>(status, errorResult), status);
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
