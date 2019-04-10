package com.easipass.sys.service.impl;

import com.easipass.sys.entity.CommSysConfigEntity;
import com.easipass.sys.service.CommConfigService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("commConfigService")
public class CommConfigServiceImpl extends BaseDaoService implements CommConfigService {


	@Override
	public List<CommSysConfigEntity> loadConfig() {//常用参数读取
		List<CommSysConfigEntity> configList = commConfigDao.findAll();
		return configList;
	}

	@Override
	public CommSysConfigEntity findConfigByConfigName(String key) {
		return commConfigDao.findConfig(key);
	}

	@Override
	public String findValueByConfigName (String key) {
		return commConfigDao.findConfig(key).getParaValue();
	}


	public void insert(Object o) throws Exception {
		commConfigDao.insert(o);
	}

	public void update(Object o) throws Exception {
		commConfigDao.update(o);
	}

	public void deleteEntityById(List ids,Class entity) {
		commConfigDao.delete(entity,ids);
	}

}
