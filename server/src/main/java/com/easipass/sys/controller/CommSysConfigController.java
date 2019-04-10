package com.easipass.sys.controller;

import com.easipass.annotation.EntityClass;
import com.easipass.sys.entity.CommSysConfigEntity;
import com.easipass.sys.util.ApiResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("commConfig")
@EntityClass(CommSysConfigEntity.class)
public class CommSysConfigController extends BaseController  {

    /**
     * 常用参数查询
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "search")
    public ApiResult search(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("---------------常用参数查询");
        ApiResult result = this.queryBasicPage(map);
        return result;
    }

    /**
     * 常用参数增删改
     * @param request
     * @param response
     * @return
     */
    @PostMapping(value = "baseSqlHandle")
    public ApiResult baseSqlHandle(@RequestBody Map map, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info("---------------常用参数增删改");
        ApiResult result = this.cudBasicHandler(map);
        redisUtil.refreshCommconfigCache();
        return result;

    }
}
