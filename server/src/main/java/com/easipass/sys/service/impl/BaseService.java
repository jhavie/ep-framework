package com.easipass.sys.service.impl;

import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.dao.CommConfigDao;
import com.easipass.sys.dao.Page;
import com.easipass.sys.dao.Query;
import com.easipass.sys.db.ProcedureUtils;
import com.easipass.sys.db.QueryFactory;
import com.easipass.sys.exception.EasiServiceException;
import com.easipass.sys.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Description: 基础服务类
 * @author: mlzhang
 * @date: 2016/10/12 13:51
 * @version: V1.0
 */

public abstract class BaseService {

    protected final Log log = LogFactory.getLog(this.getClass());
    @PersistenceContext
    private EntityManager entityManager;

    @Resource(name = "dataSource")
    public DataSource dataSource;

    @Resource(name = "commConfigDao")
    public CommConfigDao commConfigDao;


    @Autowired
    public RestTemplate restTemplate;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public Query getQuery(Class clz) {
        Query query = Query.forClass(clz, getEntityManager());
        return query;
    }

    public Page getEntityPage(Map<String,Object> requestMap, Class entity) throws Exception{
        if(null != requestMap.remove("isAll")){
            List list = this.getEntityList(requestMap , entity);
            return new Page(list);
        }
        int offset = requestMap.get("offset")!= null ? (int)requestMap.remove("offset"):1;
        int limit = requestMap.get("limit") != null ? (int)requestMap.remove("limit"):10;
        Query query = createCriteriaQuery(entity,requestMap);
        return commConfigDao.queryPage(query,offset,limit);
    }

    public List getEntityList(Map<String,Object> requestMap, Class entity){
        Query query = createCriteriaQuery(entity,requestMap);
        return commConfigDao.query(query);
    }

    public Map queryBasicPage(Map<String, Object> requestMap, Class entity){
        Map<String, Object> data = new HashMap<>();
        try{
            Page entityPage = this.getEntityPage(requestMap,entity);
            data.put("total",entityPage.getCount());
            data.put("rows",entityPage.getResult());
        }catch (Exception e){
            log.error(e.getMessage() , e);
            throw new EasiServiceException(SysConstants.ERROR_CODE001);
        }
        return data;
    }

    public Map queryBasicList(Map<String, Object> requestMap, Class entity){
        Map<String, Object> data = new HashMap<>();
        try{
            List entityList = this.getEntityList(requestMap,entity);
            data.put("total",entityList.size());
            data.put("rows",entityList);
        }catch (Exception e){
            log.error(e.getMessage() , e);
            throw new EasiServiceException(SysConstants.ERROR_CODE001);
        }
        return data;
    }

    public Map getBasic(Map<String, Object> requestMap, Class entity){
        Map<String, Object> data = new HashMap<>();
        try{
            if(null == requestMap.get("id")){
                throw new EasiServiceException(SysConstants.ERROR_CODE101);
            }
            Object object = requestMap.get("id");
            if(object instanceof Long || object instanceof Integer){
                Long id = Long.valueOf(String.valueOf(object)) ;
                Object objectEntity = commConfigDao.get(entity,id);
                data.put("entity",objectEntity);
            }else if(object instanceof String){
                String id = String.valueOf(object) ;
                Object objectEntity = commConfigDao.get(entity,id);
                data.put("entity",objectEntity);
            }

        }catch (EasiServiceException e){
            log.error(e.getMessage() , e);
            throw e;
        }catch (Exception e){
            log.error(e.getMessage() , e);
            throw new EasiServiceException(SysConstants.ERROR_CODE001);
        }
        return data;
    }

    @Transactional
    public Map cudBasicHandler(Map<String, Object> requestMap, Class entity){
        Map<String, Object> dataMap = new HashMap<>();
        try{
            List<Map<String,Object>> updateList = null != requestMap.get("update") ? (List<Map<String,Object>>) requestMap.get("update") : new ArrayList<>();
            List<Integer> deleteList = null != requestMap.get("delete") ? (List<Integer>) requestMap.get("delete") : new ArrayList<>();
            List<Map<String,Object>> insertList = null != requestMap.get("insert") ? (List<Map<String,Object>>) requestMap.get("insert") : new ArrayList<>();
            if(updateList.size() > 0){
                for(Map<String,Object> updateMap : updateList){
                    commConfigDao.update(JsonUtil.jsonToBean(JsonUtil.beanToJson(updateMap),entity));
                }
            }
            if(deleteList.size() > 0){
                commConfigDao.delete(entity,deleteList);
            }
            if(insertList.size() > 0){
                for(Map<String,Object> insertMap : insertList){
                    commConfigDao.insert(JsonUtil.jsonToBean(JsonUtil.beanToJson(insertMap),entity));
                }
            }
            dataMap.put("success",true);
            return dataMap;
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new EasiServiceException(SysConstants.ERROR_CODE001,e.getMessage());
        }
    }

    public Query createCriteriaQuery (Class clz, Map<String, Object> map) {
        return QueryFactory.assemblyCriteriaQuery(entityManager, clz, map);
    }

    protected List<javax.persistence.Query> createQlQueryList (String qlCount, String ql, Map<String, Object> map , String qlType) {
        List<javax.persistence.Query> result = new ArrayList<>();
        int count = ql.toLowerCase().indexOf("from");
        if(StringUtils.isEmpty(qlCount)){
            qlCount = "select count(*) ";
            if (count > -1) {
                qlCount += ql.substring(count);
            } else {
                throw new RuntimeException("判断Hql无from关键字，不能拼装Hql计算个数语句!");
            }
        }
        if("SQL".equals(qlType)){
            result.add(createNativeSqlQuery(qlCount, map));
            result.add(createNativeSqlQuery(ql, map));
        }else if("HQL".equals(qlType)){
            result.add(createNativeHqlQuery(qlCount, map));
            result.add(createNativeHqlQuery(ql, map));
        }
        return result;
    }

    protected javax.persistence.Query createNativeSqlQuery (String sql, Map<String, Object> map) {
        return QueryFactory.assemblyNativeSqlQuery(entityManager, sql, map);
    }

    protected javax.persistence.Query createNativeHqlQuery (String jpql, Map<String, Object> map) {
        return QueryFactory.assemblyNativeHqlQuery(entityManager, jpql, map);
    }

    // 存储过程
    public Map<Integer,Object> queryProcedure (String procedureName,Object[] inParams, int[] outParams) throws Exception {
        return ProcedureUtils.excute(dataSource, procedureName, inParams, outParams);
    }

    public String getWebPath(HttpServletRequest request){
        String path = request.getContextPath();
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;
        log.info("--------getWebPath:"+basePath);
        return basePath;
    }
}
