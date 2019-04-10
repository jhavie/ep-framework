package com.easipass.sys.exception;

/**
 * Option单独处理，返回一个允许头
 * Created by Merjiezo on 2017/4/26.
 */
public class OptionException extends Exception {
    /**
     * <p>
     * Field code: 异常代码
     * </p>
     */
    private Integer status;

    private String message;

    public OptionException() {
    }

    /**
     * 默认构造函数
     *
     * @param code    异常代码
     * @param message 异常信息
     */
    public OptionException(Integer code, String message) {
        super(message);
        this.status = code;
    }

    public OptionException(String message) {
        super(message);
        this.message = message;
    }

    public OptionException(Integer code, String message, Throwable throwble) {
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
