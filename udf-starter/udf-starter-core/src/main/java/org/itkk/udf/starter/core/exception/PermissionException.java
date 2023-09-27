package org.itkk.udf.starter.core.exception;

/**
 * 权限异常
 */
public class PermissionException extends RuntimeException {
    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -1709475985118785714L;

    /**
     * PermissionException
     *
     * @param message message
     */
    public PermissionException(String message) {
        super(message);
    }

    /**
     * PermissionException
     *
     * @param cause cause
     */
    public PermissionException(Throwable cause) {
        super(cause);
    }

    /**
     * PermissionException
     *
     * @param message message
     * @param cause   cause
     */
    public PermissionException(String message, Throwable cause) {
        super(message, cause);
    }
}
