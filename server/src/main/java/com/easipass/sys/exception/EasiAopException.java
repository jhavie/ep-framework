package com.easipass.sys.exception;

import com.easipass.sys.constant.SysConstants;

public class EasiAopException extends RuntimeException {

    /**
     * <p>
     * Field code: 异常代码
     * </p>
     */
    private String status;

    /**
     * 默认构造函数
     *
     * @param code    异常代码
     * @param message 异常信息
     */
    public EasiAopException(String code, String message) {
        super(message);
        this.status = code;
    }

    public EasiAopException(String code) {
        super(SysConstants.showInfo(code));
        this.status = code;
    }

    public EasiAopException(String code, String message, Throwable throwble) {
        super(message, throwble);
        this.status = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String code) {
        this.status = code;
    }

}
