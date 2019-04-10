package com.easipass.sys.service.impl;

import com.easipass.oauth.dto.OAuthUserInfo;
import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.entity.CommSysConfigEntity;
import com.easipass.sys.service.CommConfigService;
import com.easipass.sys.util.JsonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.*;

public class RedisUtil {

    private static RedisUtil redisUtil;

    private static  Log log = LogFactory.getLog(RedisUtil.class);

    @Value("${easipass.oauth.config.appCode}")
    private String app_Code;

    private JedisCluster jedisCluster;

    @Value("${spring.redis.cluster.nodes}")
    private String redisNodes;

    @Value("${spring.redis.config-switch:0}")
    private String configSwitch;

    @Value("${spring.redis.jedis.pool.max-active:40}")
    private Integer maxActive;

    @Value("${spring.redis.jedis.pool.max-idle:24}")
    private Integer maxIdle;

    @Value("${spring.redis.jedis.pool.min-idle:0}")
    private Integer minIdle;

    @Value("${spring.redis.jedis.pool.max-wait:-1}")
    private Integer maxWait;

    @Value("${spring.redis.timeout:3000}")
    private Integer timeout;

    @Value("${spring.redis.max-attempts:6}")
    private Integer maxAttempts;

    @Resource(name = "commConfigService")
    private CommConfigService commConfigService;


    public static void setEx(String key, String value, int second) {
        redisUtil.jedisCluster.setex(redisUtil.app_Code + ":" + key, second, value);
    }

    public static void set(String key, String value) {
        redisUtil.jedisCluster.set(redisUtil.app_Code + ":" + key, value);
    }

    public static void hmset(String key, Map<String, String> stringStringMap) {
        redisUtil.jedisCluster.hmset(redisUtil.app_Code + ":" + key, stringStringMap);
    }

    public static String get(String key) {
        return redisUtil.jedisCluster.get(redisUtil.app_Code + ":" + key);
    }

    public static String hmget(String key , String field) {
        List<String> list = redisUtil.jedisCluster.hmget(redisUtil.app_Code + ":" + key , field);
        return list.size() > 0 ? list.get(0) : null;
    }

    public static Map<String, String> hgetAll(String key) {
        return redisUtil.jedisCluster.hgetAll(redisUtil.app_Code + ":" + key );
    }

    public static void hdel(String key , String field) {
        redisUtil.jedisCluster.hdel(redisUtil.app_Code + ":" + key, field);
    }

    public static void remove(String key) {
        redisUtil.jedisCluster.del(redisUtil.app_Code + ":" + key);
    }

    public static void expireAt (String key, int second) {
        redisUtil.jedisCluster.expireAt(redisUtil.app_Code + ":" + key, second);
    }

    public static void zadd(String key, Map<String, Double> stringDoubleMap ) {
        redisUtil.jedisCluster.zadd(redisUtil.app_Code + ":" + key, stringDoubleMap);
    }

    public static Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        return redisUtil.jedisCluster.zrangeByScoreWithScores(redisUtil.app_Code + ":" + key, min , max , offset , count);
    }

    public static ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
        return redisUtil.jedisCluster.zscan(redisUtil.app_Code + ":" + key, cursor , params);
    }


    /**
     * 初始化
     */
    public void init() {
        try{
            log.info("------------------redisNodes:"+redisNodes);
            HashSet haps = new HashSet();
//            String redisIp = commConfigService.findValueByConfigName("RadisIp");
//            String[] ipArray = redisIp.split(",");
            String[] ipArray = redisNodes.split(",");
            for(String s : ipArray){
                String[] portArray = s.split(":");
                HostAndPort hap = new HostAndPort(portArray[0], Integer.parseInt(portArray[1]));
                haps.add(hap);
            }
            GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
            genericObjectPoolConfig.setMaxIdle(maxIdle);
            genericObjectPoolConfig.setMaxTotal(maxActive);
            genericObjectPoolConfig.setMinIdle(minIdle);
            genericObjectPoolConfig.setMaxWaitMillis(maxWait);
            redisUtil = this;
            if("0".equals(configSwitch)){
                redisUtil.jedisCluster = new JedisCluster(haps);
            }else {
                redisUtil.jedisCluster = new JedisCluster(haps, timeout, maxAttempts, genericObjectPoolConfig);
            }
            log.info("redisUtil初始化...");

            refreshCommconfigCache();
        }catch (Exception e){
            log.error("---------redis初始化错误");
            log.error(e.getMessage() , e);
        }
    }


    public static String getConfig(String key){
        String value = hmget("CommConfigCache",key);
        if(null == value){
            redisUtil.refreshCommconfigCache();
            value = hmget("CommConfigCache",key);
        }
        return value;
    }

    public static String getDefaultConfig(String key , String defaultVale) {
        String value = getConfig(key);
        return null != value ? value : defaultVale;
    }

    public static Long getDefaultLongConfig(String key , Long defaultVale) {
        String value = getConfig(key);
        return null != value ? Long.valueOf(value) : defaultVale;
    }

    /***
     * 设置内容
     * @param token
     * @param oui
     */
    public static void setToken (String token, OAuthUserInfo oui) {
        String userId = oui.info.attrs.get("Userid");
        setEx(SysConstants.REDIS_RT_NAMESPACE + token, userId, SysConstants.REDIS_OUT_TIME_30);
        setEx(SysConstants.REDIS_USERID_NAMESPACE + userId, JsonUtil.beanToJson(oui), SysConstants.REDIS_OUT_TIME_30);
    }

    public static String getToken (String token) {
        String userId = get(SysConstants.REDIS_RT_NAMESPACE + token);
        if (null != userId) {
            return get(SysConstants.REDIS_USERID_NAMESPACE + userId);
        }
        return null;
    }

    /**
     * 删除OAuth
     * @param token
     * @return
     */
    public static void removeToken (String token) {
        remove(SysConstants.REDIS_RT_NAMESPACE + token);
    }



    /**
     * 常用参数缓存
     */
    public void refreshCommconfigCache() {
        List<CommSysConfigEntity> configList = commConfigService.loadConfig();
        log.info("------------------refreshCommconfigCache:"+ JsonUtil.beanToJson(configList));
        Map<String , String> map = new HashMap<>();
        for (CommSysConfigEntity config : configList) {
            String value = config.getParaValue() == null ? "" : config.getParaValue();
            map.put(config.getParaName(),value);
        }
        hmset("CommConfigCache",map);
    }

}
