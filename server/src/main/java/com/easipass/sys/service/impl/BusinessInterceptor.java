package com.easipass.sys.service.impl;

import com.easipass.oauth.dto.OAuthUserInfo;
import com.easipass.oauth.service.AppOauthService;
import com.easipass.oauth.exception.ApiNoAuthException;
import com.easipass.oauth.exception.NoOauthException;
import com.easipass.sys.exception.TrafficException;
import com.easipass.sys.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Merjiezo on 2017/6/29.
 */
@Service("businessInterceptor")
public class BusinessInterceptor {

    @Autowired
    public AppOauthService service;

    @Value("${easipass.oauth.config.appCode}")
    private static String APP_CODE;

    private Log logger = LogFactory.getLog(getClass());

    //OAuth
    private boolean lowerComparison ;
    private PathMatcher urlMatcher = new AntPathMatcher();

    public void setLowerComparison(boolean lowerComparison) {
        this.lowerComparison = lowerComparison;
    }

    //Traffic
    private static final String TRAFFIC_KEY = APP_CODE + "_AEP_FLOW_";
    //参数可以精确到月
    private static final int[] DEFAULT_TIME = { 1, 60, 3600, 86400, 2592000 };

    public OAuthUserInfo handleOAuth(HttpServletRequest request) throws NoOauthException {

        // 获取应用的Code
        String appCode = this.getAppcode(request);
        // 用户请求的refresh_token
        String newrt = request.getHeader("eptoken");
        if (newrt == null) {
            newrt = request.getParameter("eptoken");
        }
        if (newrt == null) {
            newrt = String.valueOf(request.getAttribute("eptoken"));
        } else {
            request.setAttribute("refresh_token", newrt);
        }
        logger.info("-------------epToken-----------------:"+newrt);
        request.setAttribute("refresh_token", newrt);
        OAuthUserInfo oui;

        //无token，拦截
        if (StringUtils.isEmpty(newrt)) {
            throw new NoOauthException("noOauth");
        } else {
            oui = service.getUserInfo(newrt,appCode);
            if (null != oui) {
                if (!"T".equals(oui.flag)) {
                    logger.error(JsonUtil.beanToJson(oui));
                    throw new NoOauthException("noOauth");
                }
            } else {
                logger.error("Oauth服务器返回为空！");
                throw new NoOauthException("noOauth");
            }
        }
        if (!match(request ,newrt, oui)) {
            throw new ApiNoAuthException("ApiNoAuth");
        }
        return oui;
    }

    public boolean handleTraffic (HttpServletRequest request) throws TrafficException {
        String flowRule = RedisUtil.get("TrafficConf");
        String user_LoginId = (String) request.getAttribute("UserloginId");

        if (!com.easipass.sys.util.StringUtils.isEmpty(flowRule) && !com.easipass.sys.util.StringUtils.isEmpty(user_LoginId)) {

            String key = BusinessInterceptor.TRAFFIC_KEY + user_LoginId;

            String frequency = RedisUtil.get(key);

            if (null != frequency) {

                //如果用户限流配置为traffic，则限流
                if ("traffic".equals(frequency)) {
                    throw new TrafficException("traffic");
                } else {
                    //切分节流阀配置
                    String[] userFlowTimes = frequency.split("##", 2);

                    if (!com.easipass.sys.util.StringUtils.isEmpty(userFlowTimes[0]) && !com.easipass.sys.util.StringUtils.isEmpty(userFlowTimes[1])) {
                        Long outUnixTime = Long.parseLong(userFlowTimes[1]);

                        try {

                            String conf = match(flowRule, outUnixTime, userFlowTimes[0]);
                            RedisUtil.set(key, conf);
                            RedisUtil.expireAt(key,  Integer.parseInt(String.valueOf(outUnixTime)));
                        } catch (TrafficException ex) {
                            //捕获异常traffic异常，往外抛错
                            RedisUtil.set(key, "traffic");
                            RedisUtil.expireAt(key, Integer.parseInt(String.valueOf(outUnixTime)));
                            throw new TrafficException(ex.getMessage());
                        } catch (Exception e) {
                            logger.error(e.getMessage());
                        }
                    } else {
                        logger.error("保存在缓存服务器中的节流规则异常");
                    }

                }
            } else {
                setUserFrequency(key, flowRule);
            }

        }
        return true;
    }

    private String getAppcode(HttpServletRequest httpRequest) {
        String appCode = httpRequest.getParameter("app_code");
        if (StringUtils.isEmpty(appCode)) {
            appCode = APP_CODE;
        }
        return appCode;
    }

    private boolean match(HttpServletRequest request, String token, OAuthUserInfo oui) {
        boolean matched = false;
        if (null != oui  && null != oui.info && null != oui.info.permits
                && oui.info.permits.size() > 0) {

            String url;
            String reqUrl = request.getServletPath();
            // logger.info("match method reqUrl="+reqUrl);
            if (!(request.getQueryString() == null || "null".equals(request
                    .getQueryString()))) {
                reqUrl = reqUrl + "?" + request.getQueryString();
                if (reqUrl.indexOf("eptoken=") != -1) {
                    reqUrl = reqUrl.substring(0,
                            reqUrl.indexOf("eptoken=") - 1);
                }
            }
            if (lowerComparison) {
                reqUrl = reqUrl.toLowerCase();
            }

            for (OAuthUserInfo.UserInfo.UmPermission key : oui.info.permits) {
                url = key.url;
                // 兼容旧接口
                url = url.replace(".do", "/");
                if (lowerComparison) {
                    url = url.toLowerCase();
                }

                if (urlMatcher.match(url, reqUrl)) {
                    matched = true;
                    break;
                }
            }
        }
        return matched;

    }

    /***
     * 节流阀算法
     * @param flowRule     节流配置的规则
     * @param originTime   初始时间
     * @param userFlowTime 用户的行为
     * @return Boolean     是否使其限流
     */
    private String match(String flowRule, long originTime, String userFlowTime) throws Exception {
        //初始值和规则
        StringBuffer nowUserFlow = new StringBuffer("");
        long newTime = System.currentTimeMillis() / 1000;
        String[] rules = userFlowTime.split("#"),
                flowRules = flowRule.split("#");

        if (rules.length == flowRules.length) {

            for (int i = 0, j = rules.length; i < j; i++) {
                String[] singleUserFlow = rules[i].split("\\|", 2);
                String[] singleRules = flowRules[i].split("\\|", 2);
                int second = DEFAULT_TIME[i] * Integer.parseInt(singleRules[0]);

                if (!"*".equals(singleRules[1])) {
                    long singleUserTime = Long.parseLong(singleUserFlow[0]);
                    int singleUserFrequency = Integer.parseInt(singleUserFlow[1]);

                    //判断时间过了没
                    if (singleUserTime + second >= newTime) {

                        if (singleUserFrequency + 1 <= Integer.parseInt(singleRules[1])) {
                            singleUserFrequency += 1;
                            nowUserFlow.append(singleUserTime).append("|").append(singleUserFrequency).append("#");
                        } else {
                            //超流
                            throw new TrafficException("traffic");
                        }
                    } else {
                        nowUserFlow.append(newTime).append("|").append("1#");
                    }


                } else {
                    //如果为*号，则为此
                    nowUserFlow.append(newTime).append("|").append("1#");
                }

            }
        } else {
            throw new Exception("保存的参数个数和配置表不一致");
        }
        nowUserFlow.append("#").append(originTime);
        return nowUserFlow.toString();
    }
    /***
     * 设置初始节流阀参数
     * @param key 存入Redis的主键
     * @param flowRule 规则匹配
     */
    private void setUserFrequency(String key, String flowRule) {
        String[] rules = flowRule.split("#");
        int length = rules.length ,
                time_out = Integer.valueOf(RedisUtil.get("TrafficTimeOutTime"));
        long nowTime = System.currentTimeMillis() / 1000,
                outUnixTime = nowTime + time_out;

        StringBuffer val = new StringBuffer();

        while (length > 0) {
            val.append(nowTime).append("|1#");
            length--;
        }
        val.append("#").append(outUnixTime);

        RedisUtil.setEx(key, val.toString(), time_out);
    }
}
