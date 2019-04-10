package com.easipass.sys.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by YT on 2017/3/10.
 */
public class NetUtils {

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            + "|windows (phone|ce)|blackberry"
            + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            + "|laystation portable)|nokia|fennec|htc[-_]"
            + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    //移动设备正则匹配
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    //安卓
    static String androidReg = "\\bandroid|Nexus\\b";
    //ios
    static String iosReg = "ip(hone|od|ad)";

    static Pattern androidPat = Pattern.compile(androidReg, Pattern.CASE_INSENSITIVE);
    static Pattern iosPat = Pattern.compile(iosReg, Pattern.CASE_INSENSITIVE);

    private static final Log log = LogFactory.getLog(NetUtils.class);

    /**
     * 根据请求的USER-AGENT获取用户访问的客户端类别(微信/安卓/IOS/其他移动设备)
     *
     * @param request
     * @return
     */
    public static String getClientType(HttpServletRequest request) {
        String userAgent = request.getHeader("USER-AGENT").toLowerCase();
        if (Utils.isNull(userAgent)) {
            userAgent = StringUtils.BLANK;
        }
        // 匹配
        Matcher matcherAndroid = androidPat.matcher(userAgent);
        Matcher matcherIos = iosPat.matcher(userAgent);
        String result = BConstants.CLIENT_TYPE_MC;
        if (userAgent.indexOf("micromessenger") > 0) { // 是微信浏览器
            result = BConstants.CLIENT_TYPE_WX;
        } else if (matcherAndroid.find()) {
            result = BConstants.CLIENT_TYPE_ANDROID;
        } else if (matcherIos.find()) {
            result = BConstants.CLIENT_TYPE_IOS;
        }

        return result;
    }

    /**
     * 检测是否是移动设备访问
     *
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     * @Title: check
     */
    public static boolean check(String userAgent) {
        if (Utils.isNull(userAgent)) {
            userAgent = StringUtils.BLANK;
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);

        return matcherPhone.find() || matcherTable.find();
    }

    /**
     * 检查访问方式是否为移动端
     *
     * @param request
     * @return: String
     */
    public static String checkClientType(HttpServletRequest request) {
        //返回标识
        String result = BConstants.CLIENT_TYPE_PC;
        try {
            //获取USER-AGENT来判断是否为移动端访问
            String userAgent = request.getHeader("USER-AGENT").toLowerCase();
            if (Utils.isNull(userAgent)) {
                userAgent = StringUtils.BLANK;
            }
            //判断是否为移动端访问
            if (NetUtils.check(userAgent)) {
                result = BConstants.CLIENT_TYPE_MC;
                log.info("移动设备访问");
            } else {
                log.info("PC设备访问");
            }
        } catch (Exception ex) {
            log.error("判断请求客户端类型时发生异常:" + ex.getMessage(), ex);
        } finally {
            return result;
        }
    }
}
