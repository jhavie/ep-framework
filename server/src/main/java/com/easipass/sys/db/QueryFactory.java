package com.easipass.sys.db;


import com.easipass.sys.dao.Query;
import com.easipass.sys.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Query工厂类方法(Query, com.easipass.sys.dao.Query)
@Slf4j
public class QueryFactory {

    public static javax.persistence.Query assemblyNativeSqlQuery (EntityManager entityManager, String sql, Map whereParams) {
        Map<String, Object> sqlMap = new HashMap();
        StringBuffer qlbf = new StringBuffer(sql);
        QueryFactory.toQueryStr(qlbf, whereParams, sqlMap, "SQL");
        javax.persistence.Query query = entityManager.createNativeQuery(qlbf.toString());
        if (!sqlMap.isEmpty()) {
            for (String key : sqlMap.keySet()) {
                query.setParameter(key, sqlMap.get(key));
            }
        }
        return query;
    }

    /***
     *
     * @param entityManager EntityManager
     * @param jpql sql | hql 语句
     * @param whereParams where条件
     * @return javax.persistence.Query
     */
    public static javax.persistence.Query assemblyNativeHqlQuery (EntityManager entityManager, String jpql, Map whereParams) {
        Map<String, Object> hqlMap = new HashMap();
        StringBuffer qlbf = new StringBuffer(jpql);
        QueryFactory.toQueryStr(qlbf, whereParams, hqlMap, "HQL");
        javax.persistence.Query query = entityManager.createQuery(qlbf.toString());
        if (!hqlMap.isEmpty()) {
            for (String key : hqlMap.keySet()) {
                query.setParameter(key, hqlMap.get(key));
            }
        }
        return query;
    }

    /***
     * 拼装成Query
     * @param entityManager EntityManager
     * @param clazz 实体类
     * @param whereParams where条件
     * @return com.easipass.sys.Query
     */
    public static Query assemblyCriteriaQuery (EntityManager entityManager, Class clazz, Map<String, Object> whereParams) {
        String orderBy = "";
        String groupBy = "";
        Query query = Query.forClass(clazz, entityManager);
        for (String key : whereParams.keySet()) {
            String[] word = key.split("_");
            switch (word[0]) {
                case "orderBy":
                    orderBy = (String) whereParams.get(key);
                    break;
                case "groupBy":
                    groupBy = (String) whereParams.get(key);
                    break;
                case "eq":
                    query.eq(word[1], whereParams.get(key));
                    break;
                case "isNull":
                    query.isNull(word[1]);
                    break;
                case "isNotNull":
                    query.isNotNull(word[1]);
                    break;
                case "notEq":
                    query.notEq(word[1], whereParams.get(key));
                    break;
                case "notIn":
                    query.notIn(word[1], (Collection) whereParams.get(key));
                    break;
                case "like":
                    query.like(word[1], (String) whereParams.get(key));
                    break;
                case "le":
                    query.le(word[1], (Number) whereParams.get(key));
                    break;
                case "lt":
                    query.lt(word[1], (Number) whereParams.get(key));
                    break;
                case "ge":
                    query.ge(word[1], (Number) whereParams.get(key));
                    break;
                case "gt":
                    query.gt(word[1], (Number) whereParams.get(key));
                    break;
                case "in":
                    query.in(word[1], (Collection) whereParams.get(key));
                    break;
                case "between":
                    try {
                        if (!String.valueOf(whereParams.get(key)).equals("Invalid date,Invalid date") && !String.valueOf(whereParams.get(key)).equals("")) {
                            String startValue = (((String) whereParams.get(key)).split(","))[0];
                            String endValue = (((String) whereParams.get(key)).split(","))[1];
                            query.between(word[1], (Date) QueryUtils.changeStringToDate(startValue, "0"), (Date) QueryUtils.changeStringToDate(endValue, "1"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        if (!groupBy.equals("")) {
            String[] groupbyArr = groupBy.split(",");
            for(int i = 0 ; i < groupbyArr.length ; i++){
                query.setGroupBy(groupbyArr[i]);
            }
        }
        if (!orderBy.equals("")) {
            String[] orderbyArr = orderBy.split(",");
            for(int i = 0 ; i < orderbyArr.length ; i = i + 2) {
                query.addOrder(orderbyArr[i], orderbyArr[i + 1]);
            }
        }
        return query;
    }

    // 复杂情况专用
    public static void toQueryStr (StringBuffer qlbuf, Map<String, Object> map, Map<String, Object> qlMap, String qlType) {
        if (!qlbuf.toString().toLowerCase().contains("where")) {
            qlbuf.append(" ").append("where 1 = 1");
        }
        String orderBy = "";
        String groupBy = "";
        if (null != map) {
            for (String key : map.keySet()) {
                Object param = map.get(key);
                if (!StringUtils.isEmpty(param)) {
                    String[] word = key.split("_");
                    String qlKey;
                    String keyParam;
                    if ("orderBy".equals(key)) {
                        orderBy = (String) map.get(key);
                    } else if ("groupBy".equals(key)) {
                        groupBy = (String) map.get(key);
                    } else {
                        if (word.length == 2) {
                            if(qlType.equals("SQL")){
                                qlKey = QueryUtils.replaceWithLine(word[1]);
                                keyParam = QueryUtils.replaceWithLine(word[1]);
                            }else {
                                qlKey = word[1];
                                keyParam = word[1];
                            }
                        } else if (word.length == 3) {
                            // 拼接搜索表缩写
                            if(qlType.equals("SQL")){
                                qlKey = word[1] + '.' + QueryUtils.replaceWithLine(word[2]);
                                keyParam = word[1] + QueryUtils.replaceWithLine(word[2]);
                            }else{
                                qlKey = word[1] + '.' + word[2];
                                keyParam = word[1] + word[2];
                            }
                        } else {
                            // 无法拼接，直接继续
                            log.info("无法拼接内容[" + key + "]");
                            continue;
                        }
                        switch (word[0]) {
                            case "eq":
                            case "or":
                            case "orLike":
                                qlbuf.append(" and ").append(qlKey).append("=").append(":").append(keyParam);
                                qlMap.put(keyParam, param);
                                break;
                            case "notEq":
                                qlbuf.append(" and ").append(qlKey).append("!=").append(":").append(keyParam);
                                qlMap.put(keyParam, param);
                                break;
                            case "like":
                                qlbuf.append(" and ").append(qlKey)
                                        .append(" ").append(word[0]).append(" ")
                                        .append(":").append(keyParam);
                                qlMap.put(keyParam, param);
                                break;
                            case "le":
                                handleQlbufQlmapAppendWithDate("<=", qlbuf, qlMap, qlKey, keyParam, param);
                                break;
                            case "lt":
                                handleQlbufQlmapAppendWithDate("<", qlbuf, qlMap, qlKey, keyParam, param);
                                break;
                            case "ge":
                                handleQlbufQlmapAppendWithDate(">=", qlbuf, qlMap, qlKey, keyParam, param);
                                break;
                            case "gt":
                                handleQlbufQlmapAppendWithDate(">", qlbuf, qlMap, qlKey, keyParam, param);
                                break;
                            case "in":
                                String inbuf;
                                try{
                                    inbuf =  " and " + qlKey + " " + word[0] + " (" + org.apache.commons.lang3.StringUtils.join(QueryUtils.objectArrayChange(param),",") + ")";
                                    qlbuf.append(inbuf);
                                }catch (ClassCastException e){
                                    inbuf =  " and " + qlKey + " " + word[0] + " (" + org.apache.commons.lang3.StringUtils.join(QueryUtils.objectArrayChange(param.toString().split(",")),",") + ")";
                                    qlbuf.append(inbuf);
                                }
                                break;
                            case "between":
                                try {
                                    if(!String.valueOf(map.get(key)).equals("Invalid date,Invalid date")
                                            && !String.valueOf(map.get(key)).equals("")){
                                        String startValue = ((String)map.get(key)).split(",")[0];
                                        String endValue = ((String)map.get(key)).split(",").length > 1 ? ((String)map.get(key)).split(",")[1] : "";
                                        if(!"".equals(startValue) && !"".equals(endValue)){
                                            String startDateKey = keyParam + "StartDate";
                                            String endDateKey = keyParam + "endDate";
                                            qlbuf.append(" and ").append(qlKey).append(" ").append(word[0]).append(" ")
                                                    .append(":").append(startDateKey).append(" and ").append(":").append(endDateKey);
                                            qlMap.put(startDateKey, QueryUtils.changeStringToDate(startValue , "0"));
                                            qlMap.put(endDateKey, QueryUtils.changeStringToDate(endValue , "1"));

                                        } else if("".equals(startValue)) {
                                            String endDateKey = keyParam + "endDate";
                                            qlbuf.append(" and ").append(qlKey).append(" ").append("<=").append(" ").append(":").append(endDateKey);
                                            qlMap.put(endDateKey, QueryUtils.changeStringToDate(endValue , "0"));
                                        } else if("".equals(endValue)) {
                                            String startDateKey = keyParam + "StartDate";
                                            qlbuf.append(" and ").append(qlKey).append(" ").append(">=").append(" ").append(":").append(startDateKey);
                                            qlMap.put(startDateKey, QueryUtils.changeStringToDate(endValue , "0"));
                                        }

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }

            }
        }
        if (!"".equals(groupBy)) {
            String[] groupbyArr = groupBy.split(",");
            qlbuf.append(" group by ");
            for(int i = 0 ; i < groupbyArr.length ; i++){
                qlbuf.append(groupbyArr[i] + " ," );
            }
            qlbuf.deleteCharAt(qlbuf.length() - 1);
        }
        if (!"".equals(orderBy)) {
            String[] orderbyArr = orderBy.split(",");
            qlbuf.append(" order by ");
            for(int i = 0 ; i < orderbyArr.length ; i = i + 2){
                qlbuf.append(orderbyArr[i] + " " + orderbyArr[i + 1] + " ,"  );
            }
            qlbuf.deleteCharAt(qlbuf.length() - 1);
        }
    }

    /***
     * @param symbols 符号
     * @param qlbuf sql语句
     * @param qlMap Sql / Hql map
     * @param qlKey
     * @param keyParam
     * @param param
     */
    private static void handleQlbufQlmapAppendWithDate (String symbols, StringBuffer qlbuf, Map<String, Object> qlMap,
                                                         String qlKey, String keyParam, Object param) {
        qlbuf.append(" and ").append(qlKey)
                .append(" ").append(symbols).append(" ")
                .append(":").append(keyParam);
        qlMap.put(keyParam, QueryUtils.changeStringToDate(param , "0"));
    }
}
