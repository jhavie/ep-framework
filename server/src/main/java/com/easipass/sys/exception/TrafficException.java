package com.easipass.sys.exception;

/**
 * 节流阀异常类
 * Created by Merjiezo on 2017/4/19.
 */
public class TrafficException extends Exception {

    /**
     * <p>
     * Field code: 异常代码
     * </p>
     */
    private Integer status;

    private String message;

    public TrafficException() {
    }

    /**
     * 默认构造函数
     *
     * @param code    异常代码
     * @param message 异常信息
     */
    public TrafficException(Integer code, String message) {
        super(message);
        this.status = code;
    }

    public TrafficException(String message) {
        super(message);
        this.message = message;
    }

    public TrafficException(Integer code, String message, Throwable throwble) {
        super(message, throwble);
        this.status = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer code) {
        this.status = code;
    }

    public String getMessage() {
        return null == message ? super.getMessage() : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
