package com.easipass.sys.exception;

/**
 * Created by Merjiezo on 2017/7/10.
 */
public class OptimisticLockingException extends RuntimeException {

    /**
     * <p>
     * Field code: 异常代码
     * </p>
     */
    private String status;

    private String message;

    public OptimisticLockingException() {
    }

    /**
     * 默认构造函数
     *
     * @param code    异常代码
     * @param message 异常信息
     */
    public OptimisticLockingException(String code, String message) {
        super(message);
        this.status = code;
    }

    public OptimisticLockingException(String message) {
        super(message);
        this.message = message;
    }

    public OptimisticLockingException(String code, String message, Throwable throwble) {
        super(message, throwble);
        this.status = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String code) {
        this.status = code;
    }

    public String getMessage() {
        return null == message ? super.getMessage() : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
