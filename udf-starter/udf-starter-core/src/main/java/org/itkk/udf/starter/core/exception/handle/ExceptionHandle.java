package org.itkk.udf.starter.core.exception.handle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.itkk.udf.starter.core.*;
import org.itkk.udf.starter.core.exception.AuthException;
import org.itkk.udf.starter.core.exception.ParameterValidException;
import org.itkk.udf.starter.core.exception.PermissionException;
import org.itkk.udf.starter.core.exception.SystemRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class ExceptionHandle extends ResponseEntityExceptionHandler {

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
     * 请求对象
     */
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 响应对象
     */
    @Autowired
    private HttpServletResponse httpServletResponse;

    /**
     * 异常通知
     */
    @Autowired(required = false)
    private IExceptionAlert iExceptionAlert;

    /**
     * 异常处理
     *
     * @param req req
     * @param e   e
     * @return ResponseEntity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handle(WebRequest req, Exception e) {
        HttpStatus httpStatus = null;
        HttpHeaders headers = null;
        Object body = null;
        try {
            ResponseEntity<Object> objectResponseEntity = handleException(e, req);
            httpStatus = objectResponseEntity.getStatusCode();
            headers = objectResponseEntity.getHeaders();
            body = objectResponseEntity.getBody();
        } catch (Exception ex) {
            log.debug("not system error : {}", e.getMessage());
        }
        return this.handleExceptionInternal(e, body, headers, httpStatus, req);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        //处理发生异常后无法跨域的问题
        CoreUtil.fixCors(httpServletResponse, httpServletRequest, coreProperties);
        //构造错误详情
        ErrorDetail errorDetail = buildErrorDetail(ex);
        //初始化http状态
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        //异常类型判断
        if (ex instanceof PermissionException) { //权限错误
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof AuthException) { //认证错误 (返回401,前端会无法正常获取响应,这里改为返回403)
            status = HttpStatus.FORBIDDEN;
        } else if (ex instanceof ParameterValidException) { //校验错误
            status = HttpStatus.BAD_REQUEST;
        } else if (ex instanceof RestClientResponseException) { //rest服务调用错误
            try {
                RestClientResponseException restClientResponseException = (RestClientResponseException) ex;
                String data = restClientResponseException.getResponseBodyAsString();
                if (StringUtils.isNotBlank(data)) {
                    RestResponse<String> child = internalObjectMapper.readValue(data, internalObjectMapper.getTypeFactory().constructParametricType(RestResponse.class, String.class));
                    errorDetail.setChild(child);
                }
            } catch (IOException e) {
                throw new SystemRuntimeException(e);
            }
        } else if (ex instanceof MethodArgumentNotValidException) { //参数校验错误
            MethodArgumentNotValidException methodArgumentNotValidException = (MethodArgumentNotValidException) ex;
            try {
                errorDetail.setMessage(internalObjectMapper.writeValueAsString(methodArgumentNotValidException.getBindingResult().getAllErrors()));
            } catch (JsonProcessingException e) {
                throw new SystemRuntimeException(e);
            }
        }
        //判断排除条件
        if (StringUtils.isNotBlank(coreProperties.getExceptionDetailLogExclude()) && coreProperties.getExceptionDetailLogExclude().indexOf(ex.getClass().getName()) != -1) {
            //日志输出(警告)
            log.warn(CoreUtil.getTraceId() + " warn log : {}", ex.getMessage());
        } else {
            //异常通知
            if (iExceptionAlert != null) {
                iExceptionAlert.alert(CoreUtil.getTraceId(), ex);
            }
            //日志输出(错误)
            log.error(CoreUtil.getTraceId() + " error log : ", ex);
        }
        //构造响应对象
        RestResponse<String> restResponse = new RestResponse<String>()
                .setCode(CoreConstant.REST_RESPONSE_STATUS.FIAL.value())
                .setResult(CoreConstant.FAIL_MSG)
                .setErrorDetail(errorDetail);
        //如果关闭了异常堆栈输出,则将异常堆栈设置为空
        if (!coreProperties.isOutputExceptionStackTrace()) {
            restResponse.getErrorDetail().setStackTrace(null);
        }
        //调用框架方法完成返回
        return super.handleExceptionInternal(ex, restResponse, headers, status, request);
    }

    /**
     * 构造错误详情
     *
     * @param throwable throwable
     * @return ErrorDetail
     */
    public static ErrorDetail buildErrorDetail(Throwable throwable) {
        return new ErrorDetail()
                .setDate(new Date())
                .setType(throwable.getClass().getName())
                .setMessage(throwable.getMessage())
                .setStackTrace(ExceptionUtils.getStackTrace(throwable));
    }
}
