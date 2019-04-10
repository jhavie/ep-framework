package com.easipass.sys.dao;

import com.google.common.base.Strings;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;

/**
 * @Description: 接口实现了EpRepository类
 * @author: mlzhang
 * @date: 2016/10/12 19:35
 * @version: V1.0
 */

@SuppressWarnings("SpringJavaAutowiringInspection")
public class EpJpaRepository<T, ID extends Serializable>
        extends SimpleJpaRepository<T, ID> implements EpRepository<T, ID> {
    protected final Log log = LogFactory.getLog(this.getClass());

    private final EntityManager entityManager;

    public EpJpaRepository(Class<T> domainClass, EntityManager em) {
        super(domainClass, em);
        entityManager=em;
    }

    public EpJpaRepository(final JpaEntityInformation<T, ?> entityInformation, final EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /** 每次批量操作数 */
    private int batchSize = 50;

    /** 设置每次操作数 */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * 插入记录
     *
     * @param entity
     *            要插入的记录
     */
    public void insert(Object entity) {
        if (entity instanceof List) {
            insertList((List) entity);
            return;
        } else if (entity instanceof Object[]) {
            return;
        }
        entityManager.persist(entity);
    }

    /**
     * 批量增加
     *
     * @param list
     *            要新增的数据
     */
    public void insertList(List list) {
        if (list == null || list.size() == 0) {
            return;
        }
        int i = 0;
        for (Object o : list) {
            insert(o);
            if (i % batchSize == 0) {
                entityManager.flush();
            }
            i++;
        }
        log.debug(list.get(0).getClass() + "批量增加数据" + i + "条");
    }

    public <E> E get(Class clazz, Serializable id) {
        return (E) entityManager.find(clazz, id);
    }

    /**
     * 更新记录
     *
     * @param entity 要更新的记录
     *
     */
    public void update(Object entity) {
        if (entity instanceof List) {
            this.updateList((List) entity);
            return;
        }
        entityManager.merge(entity);
    }

    /**
     * 更新list
     */
    public void updateList(List list) {
        for (Object entity : list) {
            this.update(entity);
        }
    }

    public <E extends Serializable> List<E> query(String jpql) {
        return entityManager.createQuery(jpql).getResultList();
    }

    public Integer updateJpql(String jpql) {
        return entityManager.createQuery(jpql).executeUpdate();
    }

    public Integer updateSql(String sql) {
        return entityManager.createNativeQuery(sql).executeUpdate();
    }

    public <E extends Serializable> List<E> queryBySql(String sql) {
        return entityManager.createNativeQuery(sql).getResultList();
    }

    /**
     * 查询记录
     *
     * @param clazz        要查询的实体类
     * @param hqlCondition 查询条件
     */
    public <E extends Serializable> List<E> query(Class clazz, String hqlCondition) {
        return entityManager.createQuery("select t from " + clazz.getName() + " as t where " + hqlCondition)
                .getResultList();
    }


//    相当于 指定实体（表） 然后 where 条件 删除
    public void delete(Class entity, String jpqlCondition) {
        if (Strings.isNullOrEmpty(jpqlCondition)) {
            jpqlCondition = "1=1";
        }
        int no = updateJpql("delete " + entity.getName() + " where " + jpqlCondition);
        log.debug(entity.getName() + "删除" + no + "条数据");
    }





    /**
     * 根据ids删除数据
     *
     * @param entity 删除实体类
     * @param ids    删除条件
     */
    public void delete(Class entity, List ids) {
        String idName = getIdName(entity);
        StringBuffer sb = new StringBuffer();
        sb.append(idName + " in(");
        for (int i = 0; i < ids.size(); i++) {
            sb.append("'" + ids.get(i) + "',");
        }
        String jpqlCondition = sb.substring(0, sb.length() - 1) + ")";
        delete(entity, jpqlCondition);
    }

    public <E extends Serializable> List<E> query(String jpql, int firstResult, int maxResults) {
        List result = entityManager.createQuery(jpql).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
        return result;
    }

    public <E extends Serializable> List<E> queryBySql(String sql, int firstResult, int maxResults) {

        return entityManager.createNativeQuery(sql).setFirstResult(firstResult).setMaxResults(maxResults)
                .getResultList();
    }



    public <E extends Serializable> List<E> queryAll(Class clazz) {
        CriteriaQuery criteriaQuery = entityManager.getCriteriaBuilder().createQuery(clazz);
        criteriaQuery.from(clazz);
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    public int caculatePageNum(int count, int firstResult ,int pageNo, int rowsPerPage){
        if (pageNo <= 0)
            pageNo = 1;
        if (rowsPerPage <= 0)
            rowsPerPage = 7;
        log.debug("-----开始查询,页码:" + pageNo + ",每页显示:" + rowsPerPage + "----");

        // 当把最后一页数据删除以后,页码会停留在最后一个上必须减一
        int totalPageCount = count / rowsPerPage;
        if (pageNo > totalPageCount && (count % rowsPerPage == 0)) {
            pageNo = totalPageCount;
        }
        if (pageNo - totalPageCount > 2) {
            pageNo = totalPageCount + 1;
        }
        firstResult = (pageNo - 1) * rowsPerPage;
        if (firstResult < 0) {
            firstResult = 0;
        }

        return firstResult;
    }

    public Page queryPageByJpql(String jpql, int pageNo, int rowsPerPage) {

        int firstResult = 0;
        String countJpql = "select count(*) " + jpql;
        int count = getCount(countJpql).intValue();
        firstResult = caculatePageNum(count,firstResult,pageNo,rowsPerPage);

        List result = entityManager.createQuery(jpql).setFirstResult(firstResult).setMaxResults(rowsPerPage)
                .getResultList();
        return new Page(count, pageNo, rowsPerPage, result);
    }

    public Page queryPageByQuery (List<javax.persistence.Query> list, int pageNo, int rowsPerPage) {
        int firstResult = 0;
        int count = getCountByQuery(list.get(0)).intValue();
        firstResult = caculatePageNum(count, firstResult, pageNo, rowsPerPage);
        List result = list.get(1).setFirstResult(firstResult).setMaxResults(rowsPerPage)
                .getResultList();
        return new Page(count, pageNo, rowsPerPage, result);
    }

    public Page queryPageBySql(String sql, String jpql, int pageNo, int rowsPerPage) {
        int firstResult = 0;
        String countJpql = "select count(*) " + jpql;
        int count = getCount(countJpql).intValue();
        firstResult = caculatePageNum(count,firstResult,pageNo,rowsPerPage);
        log.info("//////////////"+firstResult + rowsPerPage);

        List result = entityManager.createNativeQuery(sql).setFirstResult(firstResult).setMaxResults(rowsPerPage).getResultList();
        return new Page(count, pageNo, rowsPerPage, result);

    }

    public Long getCount(String jpql) {
        return (Long) entityManager.createQuery(jpql).getResultList().get(0);
    }

    @Override
    public Long getCountByQuery(javax.persistence.Query query) {
        return Long.valueOf(query.getResultList().get(0).toString());
    }

    /***
     *
     * @Method updateJpql
     * @Description 根据传入的带有占位符的sql语句, 做增删改操作 例如
     *              updateJpql("update user t set t.name=? where t.id=?"
     *              ,{[zx],[23]})
     * @param jpql
     *            占位符式的sql
     * @param paramList
     *            list里面装有[zx, 23]
     */
    public void updateJpql(String jpql, List paramList) {
        javax.persistence.Query query = entityManager.createQuery(jpql);
        for (int i = 0; i < paramList.size(); i++) {
            query.setParameter(i + 1, paramList.get(i));
        }
        query.executeUpdate();
    }


    /**
     * 统计记录
     *
     * @param query 统计条件
     */
    public Long getCount(Query query) {
        Selection selection = query.getCriteriaQuery().getSelection();
        query.getCriteriaQuery().select(query.getCriteriaBuilder().count(query.getFrom()));
        Long count = (Long) entityManager.createQuery(query.newCriteriaQuery()).getResultList().get(0);
        query.getCriteriaQuery().select(selection);
        return count;
    }

    /**
     * 分页查询
     *
     * @param query       查询条件
     * @param pageNo      页号
     * @param rowsPerPage 每页显示条数
     */
    public Page queryPage(Query query, int pageNo, int rowsPerPage) {
        if (pageNo <= 0)
            pageNo = 1;
        if (rowsPerPage <= 0)
            rowsPerPage = 7;
        log.debug(query.getClazz() + "-----开始查询,页码:" + pageNo + ",每页显示:" + rowsPerPage + "----");
        log.debug("查询条件:");
        for (Predicate cri : query.getPredicates())
            log.debug(cri);

        int count = getCount(query).intValue();

        // 当把最后一页数据删除以后,页码会停留在最后一个上必须减一
        int totalPageCount = count / rowsPerPage;
        if (pageNo > totalPageCount && (count % rowsPerPage == 0)) {
            pageNo = totalPageCount;
        }
        if (pageNo - totalPageCount > 2) {
            pageNo = totalPageCount + 1;
        }
        int firstResult = (pageNo - 1) * rowsPerPage;
        if (firstResult < 0) {
            firstResult = 0;
        }
        List result = entityManager.createQuery(query.newCriteriaQuery()).setFirstResult(firstResult)
                .setMaxResults(rowsPerPage).getResultList();
        return new Page(count, pageNo, rowsPerPage, result);
    }

    /**
     * 根据query查找记录
     *
     * @param query       查询条件
     * @param firstResult 起始行
     * @param maxResults  结束行
     */
    public <E extends Serializable> List<E> query(Query query, int firstResult, int maxResults) {
        List result = entityManager.createQuery(query.newCriteriaQuery()).setFirstResult(firstResult)
                .setMaxResults(maxResults).getResultList();
        return result;
    }

    /**
     * 根据query查找记录
     *
     * @param query 查询条件
     */
    public <E extends Serializable> List<E> query(Query query) {
        return entityManager.createQuery(query.newCriteriaQuery()).getResultList();
    }

    /**
     * 获得主键名称
     *
     * @param clazz         操作是实体对象
     * @return 初建名称
     */
    public String getIdName(Class clazz) {
        EntityType entityType = entityManager.getMetamodel().entity(clazz);
        return entityType.getId(entityType.getIdType().getJavaType()).getName();
    }

    @Override
    public String selectSeq(String seqName){
        String sql = MessageFormat.format("select {0}.nextval as sn from dual", seqName);
        return String.valueOf(this.queryBySql(sql).get(0));
    }



}
