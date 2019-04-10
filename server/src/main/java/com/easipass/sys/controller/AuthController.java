package com.easipass.sys.controller;

import com.easipass.oauth.dto.OAuthResponse;
import com.easipass.oauth.dto.OAuthUserInfo;
import com.easipass.oauth.service.AppOauthService;
import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.entity.GccGdwsSysMenuEntity;
import com.easipass.sys.entity.MenuEntity;
import com.easipass.sys.exception.EasiControllerException;
import com.easipass.sys.service.impl.GccGdwsSysMenuService;
import com.easipass.sys.util.ApiResult;
import com.easipass.sys.util.JsonUtil;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Merjiezo on 2018/5/16.
 */
@RestController
public class AuthController {

    private final Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private AppOauthService appOAuthService;

    @Resource(name="gccGdwsSysMenuService")
    private GccGdwsSysMenuService gccGdwsSysMenuService;

    @PostMapping("userInfo")
    public ApiResult userInfo (OAuthUserInfo oui) {
        log.info("-------------------获取用户信息--------------------------");
        ApiResult apiE;
        Map<String,Object> result = new HashMap<>();
        try {
            List<GccGdwsSysMenuEntity> list = gccGdwsSysMenuService.getAllMenus();
            result.put("sysMenu", handleMenu(oui, list));
            result.put("userInfo", arrangeUserInfo(oui));
            apiE = ApiResult.newInstance(SysConstants.FLAG_T, null, null, result);
        } catch (Exception e) {
            log.error(e.getMessage() , e);
            apiE = ApiResult.newInstance(SysConstants.FLAG_F, null, "获取用户信息失败！", null);
        }
        return apiE;
    }

    @PostMapping("resetPass")
    public ApiResult resetPass (HttpServletRequest request,
                                OAuthUserInfo oui) {
        ApiResult result;
        String epToken = (String) request.getAttribute("refresh_token");
        String password = request.getParameter("password");
        String oldPass = request.getParameter("oldPass");
        if (null == epToken || null == password || null == oldPass) {
            throw new EasiControllerException(SysConstants.ERROR_CODE905);
        }
        OAuthResponse ori = appOAuthService.resetPass(epToken, oldPass, password);
        if (null == ori) {
            throw new EasiControllerException(SysConstants.ERROR_CODE908);
        } else {
            if ("F".equals(ori.flag)) {
                result = ApiResult.newInstance(SysConstants.FLAG_F,
                        SysConstants.ERROR_CODE908, ori.errorInfo, null);
                log.error(JsonUtil.beanToJson(result));
            } else {
                result = ApiResult.newInstance(SysConstants.FLAG_T, "", "", null);
            }
        }
        return result;
    }

    /***
     * 用户菜单权限获取
     * @param oui
     * @param list
     * @return
     */
    private List<MenuEntity> handleMenu (OAuthUserInfo oui, List<GccGdwsSysMenuEntity> list) {
        Map<String, Integer> urlMap = new HashMap<>();
        List<MenuEntity> routerJson = new ArrayList<>();
//        for (OAuthUserInfo.UserInfo.UmPermission key: oui.info.permits) {
//            urlMap.put(key.code, 1);
//        }
        urlMap.put("LANG", 1);
        urlMap.put("FUNCTIONAL", 1);
        urlMap.put("PAGER", 1);
        urlMap.put("401", 1);
        urlMap.put("404", 1);
        urlMap.put("WIDGETS", 1);
        urlMap.put("TABLE", 1);
        urlMap.put("DATAOPERATE", 1);
        urlMap.put("MAP", 1);
        urlMap.put("HOME", 1);
        urlMap.put("PERMISSION", 1);

        for (GccGdwsSysMenuEntity entity: list){
            String menuCode = entity.getMenuCode();
            if (urlMap.get(menuCode) != null) {
                if (entity.getParentCode().equals("0")) {
                    //父节点处理
                    routerJson.add(new MenuEntity(entity.getId(),
                            entity.getMenuUrl(), entity.getMenuName(), entity.getMenuIcon(), entity.getMenuCode()));
                    urlMap.put(entity.getMenuCode(), routerJson.size() - 1);
                } else {
                    //子节点处理
                    try {
                        int index = urlMap.get(entity.getParentCode());
                        List<MenuEntity> innerList = routerJson.get(index).getChildren();
                        innerList.add(new MenuEntity(entity.getId(),
                                entity.getMenuUrl(), entity.getMenuName(), entity.getMenuIcon(), entity.getMenuCode()));
                    } catch (NullPointerException ne) {}
                }
            }
        }
        return routerJson;
    }

    /***
     * 整理用户
     * @return
     */
    private Map<String, Object> arrangeUserInfo (OAuthUserInfo oui) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("orgName", MapUtils.getString(oui.info.attrs, "Orgname", ""));
        userInfo.put("orgCode", MapUtils.getString(oui.info.attrs, "OrgorgCode", ""));
        userInfo.put("userLoginId", MapUtils.getString(oui.info.attrs, "UserloginId", ""));
        userInfo.put("username", MapUtils.getString(oui.info.attrs, "Username", ""));
        userInfo.put("userTele", MapUtils.getString(oui.info.attrs, "Usertele", ""));
        userInfo.put("draccNo", MapUtils.getString(oui.info.attrs, "UserExtECS_DRACC_NO", ""));
        List roleList = new ArrayList<>();
        List<OAuthUserInfo.UserInfo.UmRole> userRoles = oui.info.roles;
        for (OAuthUserInfo.UserInfo.UmRole umRole: userRoles) {
            roleList.add(umRole.roleCode);
        }
        userInfo.put("roles", roleList);
        return userInfo;
    }
}
