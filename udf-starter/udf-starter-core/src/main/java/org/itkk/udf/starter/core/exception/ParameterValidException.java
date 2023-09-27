package org.itkk.udf.starter.core.exception;

import org.itkk.udf.starter.core.CoreConstant;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * 参数校验错误
 */
public class ParameterValidException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 8010752120673426846L;

    private final List<ObjectError> allErrors;

    /**
     * ParameterValidException
     *
     * @param message message
     */
    public ParameterValidException(String message) {
        super(message);
        this.allErrors = null;
    }

    /**
     * ParameterValidException
     *
     * @param allErrors allErrors
     */
    public ParameterValidException(List<ObjectError> allErrors) {
        super(CoreConstant.PARAMETER_VALID_EXCEPTION_MSG);
        this.allErrors = allErrors;
    }

    /**
     * ParameterValidException
     *
     * @param message   message
     * @param allErrors allErrors
     */
    public ParameterValidException(String message, List<ObjectError> allErrors) {
        super(message);
        this.allErrors = allErrors;
    }

    /**
     * ParameterValidException
     *
     * @param cause     cause
     * @param allErrors allErrors
     */
    public ParameterValidException(Throwable cause, List<ObjectError> allErrors) {
        super(cause);
        this.allErrors = allErrors;
    }

    /**
     * ParameterValidException
     *
     * @param message   message
     * @param cause     cause
     * @param allErrors allErrors
     */
    public ParameterValidException(String message, Throwable cause, List<ObjectError> allErrors) {
        super(message, cause);
        this.allErrors = allErrors;
    }

    /**
     * 获得所有错误
     *
     * @return List<ObjectError>
     */
    public List<ObjectError> getAllErrors() {
        return allErrors;
    }

}
