package com.easipass.sys.service.impl;

import com.easipass.oauth.dto.OAuthResponse;
import com.easipass.oauth.dto.OAuthReturnInfo;
import com.easipass.oauth.dto.OAuthUserInfo;
import com.easipass.oauth.service.AppOauthService;
import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.exception.EasiServiceException;
import com.easipass.sys.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/***
 * Created by Merjiezo on 2017/7/24.
 */
@Service("apiService")
public class ApiService extends BaseService {

    @Autowired
    public AppOauthService appOAuthService;

    @Value("${easipass.oauth.config.appCode}")
    private static String APP_CODE;

    /***
     * 登陆
     * @param requestMap
     * @return
     * @throws Exception
     */
    public Map<String, String> handleLogin (Map<String, String> requestMap) throws Exception {
        Map<String, String> res = new HashMap<>();

        String loginId = requestMap.get("id");
        String password = requestMap.get("password");
        OAuthReturnInfo ri = appOAuthService.getRefreshTokenWithCaptcha(loginId, password);

        if (null == ri) {
            throw new EasiServiceException(SysConstants.ERROR_CODE907);
        }

        if ("T".equals(ri.getFlag())) {
            OAuthUserInfo oui = appOAuthService.getUserInfo(ri.getRefreshToken(), APP_CODE);
            if (null == oui) {
                throw new EasiServiceException(SysConstants.ERROR_CODE907);
            }
            if ("T".equals(oui.flag)) {
                //用户信息放入Radis
                RedisUtil.setToken(ri.getRefreshToken(), oui);
                res.put("rt", ri.getRefreshToken());
                return res;
            } else {
                throw new EasiServiceException(SysConstants.ERROR_CODE908, oui.errorInfo);
            }
        } else {
            throw new EasiServiceException(SysConstants.ERROR_CODE908, ri.getErrorInfo());
        }
    }

    /***
     * 登出
     * @param token
     */
    public void handleLogout (String token) {
        OAuthResponse oar = appOAuthService.kickout(token);
        if (null == oar) {
            throw new EasiServiceException(SysConstants.ERROR_CODE907);
        }

        if ("F".equals(oar.flag)) {
            if ("RefreshTokenNoFind".equals(oar.errorCode)) {
                RedisUtil.removeToken(token);
            } else {
                throw new EasiServiceException(SysConstants.ERROR_CODE908,
                        oar.errorInfo);
            }
        } else {
            RedisUtil.removeToken(token);
        }
    }

    /**
     * 获取用户信息
     * @param token
     * @return
     */
    public OAuthUserInfo getOauth (String token) {
        String res = RedisUtil.get(token);
        if (res != null) {
            String userInfoStr = RedisUtil.get(SysConstants.REDIS_USERID_NAMESPACE + res);
            if (userInfoStr != null) {
                return JsonUtil.jsonToBean(userInfoStr, OAuthUserInfo.class);
            }
        }
        return null;
    }

}
