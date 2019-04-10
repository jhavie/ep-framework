package com.easipass.sys.controller;

import com.easipass.annotation.EntityClass;
import com.easipass.sys.service.CommConfigService;
import com.easipass.sys.service.impl.RedisUtil;
import com.easipass.sys.util.ApiResult;
import com.easipass.sys.constant.SysConstants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Map;

public abstract class BaseController {
	protected final Log log = LogFactory.getLog(this.getClass());

	protected Class entityClass;

	@Autowired
	public RedisUtil redisUtil;

	@Resource(name = "commConfigService")
	public CommConfigService commConfigService;

	public BaseController() {
		EntityClass entityClazz = getClass().getAnnotation(EntityClass.class);
		if (entityClazz != null) {
			try {
				this.entityClass = entityClazz.value();
			} catch (Exception e) {
				log.error(e.getMessage() , e);
			}
		} else {
			log.info("Annotation EntityClass is null.....");
		}
	}

	protected ApiResult cudBasicHandler(Map map) throws Exception {
		return ApiResult.newInstance(SysConstants.FLAG_T , null , null , commConfigService.cudBasicHandler(map , entityClass));
	}

	protected ApiResult queryBasicPage(Map map) throws Exception {
		return ApiResult.newInstance(SysConstants.FLAG_T , null , null , commConfigService.queryBasicPage(map , entityClass));
	}


	protected ApiResult queryBasicList(Map map) throws Exception {
		return ApiResult.newInstance(SysConstants.FLAG_T , null , null , commConfigService.queryBasicList(map , entityClass));
	}


	protected ApiResult getBasicById(Map map) throws Exception {
		return ApiResult.newInstance(SysConstants.FLAG_T , null , null , commConfigService.getBasic(map , entityClass));
	}


}
