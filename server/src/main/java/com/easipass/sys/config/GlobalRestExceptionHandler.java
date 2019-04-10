package com.easipass.sys.config;

import com.easipass.sys.exception.EasiAopException;
import com.easipass.sys.exception.EasiControllerException;
import com.easipass.sys.exception.EasiServiceException;
import com.easipass.sys.util.ApiResult;
import com.easipass.sys.constant.SysConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

/**
 * @author mlzhang
 */
@ControllerAdvice(annotations = {RestController.class , Controller.class})
@ResponseBody
@Slf4j
public class GlobalRestExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public ApiResult restExceptionHandler(Exception e) {
        ApiResult result;
        if (e instanceof EasiAopException) {
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    ((EasiAopException) e).getStatus(),
                    e.getMessage(),
                    null);
        } else if (e instanceof EasiControllerException) {
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    ((EasiControllerException) e).getStatus(),
                    e.getMessage(),
                    null);
        } else if (e instanceof EasiServiceException) {
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    ((EasiServiceException) e).getStatus(),
                    e.getMessage(),
                    null);
        } else if (e instanceof RestClientException) {
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    SysConstants.ERROR_CODE901,
                    "服务器异常",
                    null);
        } else {
            log.error(e.getMessage() , e);
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    SysConstants.ERROR_CODE999,
                    e.getMessage(),
                    null);
        }
        return result;
    }
}
