package org.itkk.udf.starter.core.exception;

/**
 * 系统运行时异常
 */
public class SystemRuntimeException extends RuntimeException {
    /**
     * ID
     */
    private static final long serialVersionUID = -4946012902764060933L;

    /**
     * SystemRuntimeException
     *
     * @param message message
     */
    public SystemRuntimeException(String message) {
        super(message);
    }

    /**
     * SystemRuntimeException
     *
     * @param cause cause
     */
    public SystemRuntimeException(Throwable cause) {
        super(cause);
    }

    /**
     * SystemRuntimeException
     *
     * @param message message
     * @param cause   cause
     */
    public SystemRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
