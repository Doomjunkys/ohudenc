package org.itkk.udf.starter.core.exception;

/**
 * 认证异常
 */
public class AuthException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 6176754168716606647L;

    /**
     * AuthException
     *
     * @param message message
     */
    public AuthException(String message) {
        super(message);
    }

    /**
     * AuthException
     *
     * @param cause cause
     */
    public AuthException(Throwable cause) {
        super(cause);
    }

    /**
     * AuthException
     *
     * @param message message
     * @param cause   cause
     */
    public AuthException(String message, Throwable cause) {
        super(message, cause);
    }

}
