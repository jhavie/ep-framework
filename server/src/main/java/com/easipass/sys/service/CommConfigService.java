package com.easipass.sys.service;

import com.easipass.sys.entity.CommSysConfigEntity;
import com.easipass.sys.util.ApiResult;

import java.util.List;
import java.util.Map;

public interface CommConfigService {


	List<CommSysConfigEntity> loadConfig();
	CommSysConfigEntity findConfigByConfigName(String key);
	String findValueByConfigName(String key);
	void insert(Object o) throws Exception;
	void update(Object o) throws Exception;
	void deleteEntityById(List ids, Class entity);
	Map cudBasicHandler(Map<String, Object> requestMap , Class entity);
	Map queryBasicPage(Map<String, Object> requestMap , Class entity);
	Map queryBasicList(Map<String, Object> requestMap , Class entity);
	Map getBasic(Map<String, Object> requestMap , Class entity);

}
