package com.easipass.business.aop;

import com.easipass.oauth.dto.OAuthUserInfo;
import com.easipass.oauth.exception.ApiNoAuthException;
import com.easipass.oauth.exception.NoOauthException;
import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.exception.EasiAopException;
import com.easipass.sys.service.impl.BusinessInterceptor;
import com.easipass.sys.util.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.jolokia.util.HttpMethod;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Merjiezo on 2017/6/29.
 */
@Slf4j
@Component
@Aspect
public class BusinessAspact {

    @Resource(name = "businessInterceptor")
    private BusinessInterceptor businessInterceptor;

    @Value("${settings.OauthSwitch}")
    private String oauthSwitch;

    @Value("${settings.SignSwitch}")
    private String signSwitch;

    @Value("${settings.AopRequestCharset}")
    private String aopRequestCharset;

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && " +
//            "@annotation(requestMapping) && " +
            "execution(* springfox.documentation..*.*(..))")
    public void swagger() {}

    @Pointcut("within(@org.springframework.stereotype.Controller *) && " +
            "execution(* com.easipass.business.captcha.CaptchaController.*(..))")
    public void CaptchaController() {}

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && " +
            "execution(* com.easipass.sys.controller.ApiController.*(..))")
    public void ApiController() {}

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) &&"+
            "!ApiController()" +
            " || " +
            "within(@org.springframework.stereotype.Controller *) && " +
            "!swagger() &&" +
            "!CaptchaController() " +
            "execution(* *(..))")
    public void md5AndOauthAround() {}

    @Around("ApiController()")
    public Object ApiController(ProceedingJoinPoint point) throws Throwable {
        log.debug("--------ApiController PointCut Start--------");
        HttpServletRequest request = getHttpServletRequest();
        String requestUri = request.getRequestURI();
        log.info("--------RequestUri:"+requestUri);
        //切点中获取requestBody中的值
        Object[] objects = point.getArgs();
        getData(request , objects);
        return point.proceed(objects);
    }

    @Around("md5AndOauthAround()")
    public Object md5AndOauthAround(ProceedingJoinPoint point) throws Throwable {
        log.debug("--------Md5AndOauthAround PointCut Start--------");
        HttpServletRequest request = getHttpServletRequest();
        String requestUri = request.getRequestURI();
        log.info("--------RequestUri:"+requestUri);
        //切点中获取requestBody中的值
        Object[] objects = point.getArgs();
        md5Feild(request, objects);
        oauthFeild(request , objects);
        return point.proceed(objects);
    }

    //获取request
    private HttpServletRequest getHttpServletRequest() {
        //获取request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        return  request;
    }

    //获取response
    private HttpServletResponse getHttpServletResponse(){
        //获取request
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletResponse response = sra.getResponse();
        return  response;
    }

    /***
     * 抽取验证数据，仅做数据层校验
     * [TEMP] 暂时支持GET、POST [json和form两种] 的验签校验
     * @param request HttpServletRequest 请求
     * @param objects 切面对象
     * @return String 待加签数据
     */
    private String getData(HttpServletRequest request ,Object[] objects) {
        String reqDataStr = "{}";
        Map requestBody;
        log.debug("--------Yapi:"+request.getHeader("yapi"));
        String reqMethod = request.getMethod().toUpperCase();
        if (reqMethod.equals(HttpMethod.GET.name())) {
            // GET
            reqDataStr = request.getQueryString();
            if (StringUtils.isEmpty(reqDataStr)) {
                reqDataStr = "{}";
            } else {
                try {
                    reqDataStr = URLDecoder.decode(reqDataStr, aopRequestCharset);
                } catch (UnsupportedEncodingException e) {
                    log.error("URLDecode解密失败", e);
                }
            }
        } else {
            String contentType = request.getContentType();
            if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
                // POST payload提交
                for (int j = 0 ; j < objects.length ; j++) {
                    // 对应requestBody
                    if (objects[j] instanceof LinkedHashMap) {
                        requestBody = (HashMap) objects[j];
                        log.debug("--------RequestBody:" + JsonUtil.beanToJson(requestBody));
                        reqDataStr = JsonUtil.beanToJson(requestBody);
                        break;
                    }
                }
            } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                // POST表单提交，获取epFrameworkOrder参数拼接内容获取
                String orderStr = request.getParameter("epFrameworkOrder");
                if (!StringUtils.isEmpty(orderStr)) {
                    String[] orderArr = orderStr.split(",");
                    LinkedHashMap<String, Object> mapParameter = new LinkedHashMap<>();
                    for (int i = 0; i < orderArr.length; i++) {
                        String key = orderArr[i];
                        mapParameter.put(key, request.getParameter(key));
                    }
                    reqDataStr = JsonUtil.beanToJson(mapParameter);
                    // 统一约定去除引号
                    reqDataStr = reqDataStr.replaceAll("\"", "");
                }
            }
        }
        return reqDataStr;
    }

    //md5模块
    private void md5Feild(HttpServletRequest request , Object[] objects) {
        String reqDataStr = getData(request , objects);
        log.debug("--------ReqDataStr:" + reqDataStr);
        if(signSwitch.equals("1")) {
            String sign = null != request.getHeader("epSign") ? request.getHeader("epSign") : request.getParameter("epSign"); //REQ中的签名
            String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            log.debug("--------EpSign:" + sign);
            String jMd5 = MD5Encrypt.encodeWithPrefix(reqDataStr + date);
            log.debug("--------JMd5:" + jMd5);
            if (StringUtils.isEmpty(sign) || !sign.equals(jMd5)) {
                throw new EasiAopException(SysConstants.ERROR_CODE901);
            }
        }
    }

    //OAuth模块
    private void oauthFeild(HttpServletRequest request , Object[] objects) {
        try  {
            // OAuth 验证
            OAuthUserInfo oui;
            if(oauthSwitch.equals("1")) {
                oui = businessInterceptor.handleOAuth(request);
            } else {
                oui = JsonUtil.jsonToBean(ConfUtil.getString("oauthTest.info"), OAuthUserInfo.class);
            }
            //获取成功，并且有权限，根据Controller层情况注入用户信息
            if (null != objects
                    && objects.length > 0
                    && objects[objects.length - 1].getClass() == OAuthUserInfo.class
            ) {
                OAuthUserInfo obj = (OAuthUserInfo) objects[objects.length - 1];
                obj.info = oui.info;
                obj.permission = oui.permission;
                obj.flag = oui.flag;
                obj.errorCode = oui.errorCode;
                obj.errorInfo = oui.errorInfo;
                log.info("--------注入的用户信息:"+JsonUtil.beanToJson(oui.info.attrs));
            }
        } catch (NoOauthException ne) {
            throw new EasiAopException(SysConstants.ERROR_CODE904);
        } catch (ApiNoAuthException anae) {
            throw new EasiAopException(SysConstants.ERROR_CODE906);
        }
    }

    @AfterReturning(returning = "rvt", pointcut = "within(@org.springframework.web.bind.annotation.RestController *)"+
            " || " +
            "within(@org.springframework.stereotype.Controller *) && " +
            "!swagger() " +
            "execution(* *(..))")
    public Object afterExec(JoinPoint joinPoint, Object rvt) {
        log.info("--------AfterReturningResult:"+ JsonUtil.beanToJson(rvt));
        return rvt;
    }

}
