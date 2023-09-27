package org.itkk.udf.starter.core.exception;

/**
 * 系统异常
 */
public class SystemException extends Exception {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -366270598730016249L;

    /**
     * SystemException
     *
     * @param message message
     */
    public SystemException(String message) {
        super(message);
    }

    /**
     * SystemException
     *
     * @param cause cause
     */
    public SystemException(Throwable cause) {
        super(cause);
    }

    /**
     * SystemException
     *
     * @param message message
     * @param cause   cause
     */
    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }
}
