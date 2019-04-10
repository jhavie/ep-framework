package com.easipass.sys.dao;

import com.easipass.sys.entity.CommSysConfigEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @Description: 常用参数DAO
 * @author: mlzhang
 * @date: 2016/10/10 16:53
 * @version: V1.0
 */
@Repository
@Transactional
public interface CommConfigDao extends EpRepository<CommSysConfigEntity, Long> {

    @Query("select c from CommSysConfigEntity c where c.paraName=?1")
    CommSysConfigEntity findConfig(String key);

    @Query(value = "select sysdate from dual", nativeQuery = true)
    Date getDatabaseTime();

}
