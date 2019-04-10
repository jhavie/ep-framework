package com.easipass.sys.service.impl;

import com.easipass.sys.dao.GccGdwsSysMenuDao;
import com.easipass.sys.entity.GccGdwsSysMenuEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Merjiezo on 2017/6/23.
 */
@Service("gccGdwsSysMenuService")
@Transactional
public class GccGdwsSysMenuService {

    @Resource(name = "gccGdwsSysMenuDao")
    private GccGdwsSysMenuDao gccGdwsSysMenuDao;

    public List<GccGdwsSysMenuEntity> getAllMenus() {
        return gccGdwsSysMenuDao.getAllMenus();
    }
}
