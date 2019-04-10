package com.easipass.sys.constant;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

@Slf4j
public class SysConstants {

	// Redis 命名空间
	public static final String REDIS_USERID_NAMESPACE = "userId:";
	public static final String REDIS_RT_NAMESPACE = "rt:";
	public static final String REDIS_CAPTCHA_NAMESPACE = "captcha:";

	public static final String REDIS_BIZ_TYPE_KEY = "BizType_Map";
	public static final int REDIS_OUT_TIME_5 = 5 * 60;	// 5分钟
	public static final int REDIS_OUT_TIME_30 = 1800;	// 30分钟
	public static final int REDIS_OUT_TIME = 1296000;
	public static final int REDIS_TIMEOUT = 864000;		//10天


	public static final String FLAG_T = "T";
	public static final String FLAG_F = "F";
	/**
	 * 手机号码正则表达式
	 */
	public static final String MOBILE_VERIFY_REGEX = "^1[\\d]{10}$";

	public static final String ERROR_CODE001 = "001";
	public static final String ERROR_INFO001 = "未知错误，请稍后再试";
	public static final String ERROR_CODE005 = "005";
	public static final String ERROR_INFO005 = "存储过程调用异常";

	public static final String ERROR_CODE101 = "101";
	public static final String ERROR_INFO101 = "请求参数有误或缺失";
	public static final String ERROR_CODE102 = "102";
	public static final String ERROR_INFO102 = "未找到记录";
	public static final String ERROR_CODE901 = "901";
	public static final String ERROR_INFO901 = "验证参数有误或缺失";
	public static final String ERROR_CODE902 = "902";
	public static final String ERROR_INFO902 = "验证信息失败";
	public static final String ERROR_CODE903 = "903";
	public static final String ERROR_INFO903 = "服务器异常";
	public static final String ERROR_CODE904 = "904";
	public static final String ERROR_INFO904 = "用户授权失效，请重新登录";
	public static final String ERROR_CODE905 = "905";
	public static final String ERROR_INFO905 = "认证凭据为空";
	public static final String ERROR_CODE906 = "906";
	public static final String ERROR_INFO906 = "无权访问";
	public static final String ERROR_CODE907 = "907";
	public static final String ERROR_INFO907 = "网络异常，请稍后再试！";
	public static final String ERROR_CODE908 = "908";
	public static final String ERROR_INFO908 = "OAuth异常！";
	public static final String ERROR_CODE999 = "999";
	public static final String ERROR_INFO999 = "999";

	public static String showInfo(String code) {
		String info = null;
		try{
			Class clazz = Class.forName("com.easipass.sys.constant.SysConstants");
			Field field = clazz.getField("ERROR_INFO"+code);
			info = (String) field.get(clazz);
		}catch (Exception e){
			log.error(e.getMessage() , e);
		}
		return info;
	}

}
