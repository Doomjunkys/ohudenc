package org.itkk.udf.core.exception.handle;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.itkk.udf.core.ApplicationConfig;
import org.itkk.udf.core.RestResponse;
import org.itkk.udf.core.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.Date;

/**
 * ExceptionHandle
 */
@ControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler {

    /**
     * 描述 : 系统配置
     */
    @Autowired
    private ApplicationConfig applicationConfig;

    /**
     * 描述 : objectMapper
     */
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 异常处理
     *
     * @param ex      ex
     * @param request request
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception ex, WebRequest request) {
        ResponseEntity<Object> objectResponseEntity = this.handleException(ex, request);
        return this.handleExceptionInternal(ex, null, objectResponseEntity.getHeaders(), objectResponseEntity.getStatusCode(), request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        HttpStatus localHttpStatus = status;
        ErrorResult errorResult = this.buildError(ex);
        if (ex instanceof PermissionException) { //权限异常
            localHttpStatus = HttpStatus.FORBIDDEN;
        } else if (ex instanceof AuthException) { //认证异常
            localHttpStatus = HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof ParameterValidException) { //参数校验异常
            localHttpStatus = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof RestClientResponseException) { //rest请求异常
            try {
                RestClientResponseException restClientResponseException = (RestClientResponseException) ex;
                String data = restClientResponseException.getResponseBodyAsString();
                if (StringUtils.isNotBlank(data)) {
                    RestResponse<String> child = objectMapper.readValue(data, objectMapper.getTypeFactory().constructParametricType(RestResponse.class, String.class));
                    errorResult.setChild(child);
                }
            } catch (IOException e) {
                throw new SystemRuntimeException(e);
            }
        }
        return super.handleExceptionInternal(ex, new RestResponse<>(status, errorResult), headers, localHttpStatus, request);
    }

    /**
     * 描述 : 构造错误响应对象
     *
     * @param exception 异常
     * @return 错误响应对象
     */
    private ErrorResult buildError(Exception exception) {
        ErrorResult error = new ErrorResult();
        error.setType(exception.getClass().getName());
        error.setMessage(ExceptionUtils.getMessage(exception));
        if (applicationConfig.isOutputExceptionStackTrace()) {
            error.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        error.setDate(new Date());
        logger.error(exception.getClass().getName(), exception);
        return error;
    }
}
