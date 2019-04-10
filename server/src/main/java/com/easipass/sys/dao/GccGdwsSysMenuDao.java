package com.easipass.sys.dao;

import com.easipass.sys.entity.GccGdwsSysMenuEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Merjiezo on 2017/10/20.
 */
@Repository
@Transactional
public interface GccGdwsSysMenuDao extends EpRepository<GccGdwsSysMenuEntity, Integer> {

    @Query("select c from GccGdwsSysMenuEntity c order by c.parentCode, c.menuOrder")
    List<GccGdwsSysMenuEntity> getAllMenus();
}
