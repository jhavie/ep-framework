package com.easipass.sys.controller;

import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.exception.EasiControllerException;
import com.easipass.sys.exception.EasiServiceException;
import com.easipass.sys.service.impl.ApiService;
import com.easipass.sys.service.impl.RedisUtil;
import com.easipass.sys.util.ApiResult;
import com.easipass.sys.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Merjiezo on 2017/8/8.
 */
@RestController
public class ApiController {

    private final Log log = LogFactory.getLog(this.getClass());

    @Value("${settings.CaptchaSwitch}")
    private String captchaSwitch;
    @Resource(name = "apiService")
    private ApiService apiService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/login")
    public ApiResult Login(@RequestBody Map<String,String> map, HttpServletResponse response)
            throws Exception {
        log.info("-------------------login start--------------------------");
        ApiResult result;
        String captchaRedisVal;
        try {
            // 1为校验，方便开发
            if ("1".equals(captchaSwitch)) {
                String captchaVal = map.get("captchaVal");
                String captchaUUid = map.get("captchaUUid");
                if (null != captchaVal && null != captchaUUid) {
                    captchaRedisVal = RedisUtil.get("Captcha_"+captchaUUid);
                    if (null != captchaRedisVal) {
                        captchaRedisVal = captchaRedisVal.toUpperCase();
                        if (captchaRedisVal.equals(captchaVal.toUpperCase())) {
                            Map<String, String> dataMap = apiService.handleLogin(map);
                            result = ApiResult.newInstance(SysConstants.FLAG_T,
                                    null, null, dataMap);
                            log.info(JsonUtil.beanToJson(result));
                        } else {
                            result = ApiResult.newInstance(SysConstants.FLAG_F,
                                    SysConstants.ERROR_CODE001, "验证码不正确", null);
                            log.error(JsonUtil.beanToJson(result));
                        }
                    } else {
                        result = ApiResult.newInstance(SysConstants.FLAG_F,
                                SysConstants.ERROR_CODE001, "验证码不存在或过期", null);
                        log.error(JsonUtil.beanToJson(result));
                    }
                } else {
                    throw new EasiControllerException(SysConstants.ERROR_CODE101);
                }
            } else {
                Map<String, String> dataMap = apiService.handleLogin(map);
                result = ApiResult.newInstance(SysConstants.FLAG_T,
                        null, null, dataMap);
                log.info(JsonUtil.beanToJson(result));
            }
        } catch (EasiServiceException ese) {
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    ese.getStatus(), ese.getMessage(), null);
            log.error(JsonUtil.beanToJson(result));
        }
        return result;
    }

    // 登出
    @PostMapping("/loginOut")
    public ApiResult loginOut(HttpServletRequest request) {
        log.info("-------------------loginOut start--------------------------");
        ApiResult result;

        try {
            String refreshToken = request.getParameter("refreshToken");
            if (null == refreshToken) {
                throw new EasiControllerException(SysConstants.ERROR_CODE901);
            }
            apiService.handleLogout(refreshToken);
            result = ApiResult.newInstance(SysConstants.FLAG_T,
                    null, null, "注销成功！");
            log.info(JsonUtil.beanToJson(result));
        } catch (EasiServiceException ese) {
            result = ApiResult.newInstance(SysConstants.FLAG_F,
                    ese.getStatus(), ese.getMessage(), null);
            log.error(JsonUtil.beanToJson(result));
        }
        return result;
    }

    /***
     * 获取配置项
     * @return
     */
    @PostMapping(value = "getConfig")
    public ApiResult getConfig (@RequestBody Map<String,Object> map) {
        ApiResult result;
        log.info("---------------获取配置项");
        Map<String, Object> dataMap = new HashMap<String, Object>();
        List data =  (List) map.get("configKey");
        if (null == data) {
            result = ApiResult.newInstance(SysConstants.FLAG_T, "", "", "");
            log.info(JsonUtil.beanToJson(result));
            return result;
        }
        for (Object key: data) {
            String keyStr = (String) key;
            dataMap.put(keyStr, RedisUtil.getConfig(keyStr));
        }
        result = ApiResult.newInstance(SysConstants.FLAG_T, "", "", dataMap);
        log.info(JsonUtil.beanToJson(result));
        return result;
    }

    /***
     * 刷新缓存
     * @return
     */
    @GetMapping(value = "reflushCache")
    public ApiResult reflushCache () {
        ApiResult result;
        log.info("---------------刷新缓存");
        redisUtil.refreshCommconfigCache();
        result = ApiResult.newInstance(SysConstants.FLAG_T, "", "", "");
        log.info(JsonUtil.beanToJson(result));
        return result;

    }

}
