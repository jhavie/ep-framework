package com.easipass.sys.controller;

import com.easipass.annotation.EntityClass;
import com.easipass.sys.entity.GccGdwsSysMenuEntity;
import com.easipass.sys.util.ApiResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by LH on 2017/6/8.
 */
@RestController
@RequestMapping("menu")
@EntityClass(GccGdwsSysMenuEntity.class)
public class SysMenuController extends BaseController {

    /**
     * 菜单查询
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "search")
    public ApiResult search(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("---------------菜单查询");
        ApiResult result = this.queryBasicPage(map);
        return result;
    }

    /**
     * 菜单增删改
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "baseSqlHandle")
    public ApiResult baseSqlHandle(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("---------------菜单增删改");
        ApiResult result = this.cudBasicHandler(map);
        return result;

    }
}
