package com.easipass.sys.util;


import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author xhb
 * @modity xl
 * @date 2010-10-23 下午01:52:35
 */
@SuppressWarnings({"unchecked" })
public class DateUtil {
	private static final Log log = LogFactory.getLog(DateUtil.class);

	public static final String PATTERN_FULL_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final String PATTERN_MIDUM_TIME = "yyyy-MM-dd";
	public static final String SQL_FULL_TIME = "yyyy-MM-dd hh24:mi:ss";
	public static final String PATTERN_MIDUM_DATE = "yyyyMMdd";
	public static final FastDateFormat DF_FULL= FastDateFormat.getInstance(PATTERN_FULL_TIME);
	public static final FastDateFormat DF_MIDUM = FastDateFormat.getInstance(PATTERN_MIDUM_TIME);

	/*
	加日期
	 */
	public static Date datePlus(Date oldDate , int i){
		Calendar c = Calendar.getInstance();
		c.setTime(oldDate);
		c.add(Calendar.DAY_OF_MONTH, i);// 今天+i天

		Date newDate = c.getTime();
		return newDate;
	}
	/**
	 * 字符串 转化 时间
	 * @param dateStr
	 * @return
	 */
	public static Date stringToDate(String dateStr){
		try {
			return DateUtils.parseDate(dateStr, new String[]{PATTERN_FULL_TIME,PATTERN_MIDUM_TIME});
		} catch (ParseException e) {
			log.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 获得当前时间字符串
	 * 
	 * @return
	 */
	public static String getDateString() {
		return dateToString(new Date(),PATTERN_MIDUM_TIME);
	}

	/**
	 * 获得指定时间字符串
	 * 
	 * @param d
	 * @return
	 */
	public static String getDateString(Date d) {
		return dateToString(d,PATTERN_MIDUM_TIME);
	}
	/**
	 * 获得指定时间指定格式的字符串
	 * @param d
	 * @param format
	 * @return
	 */
	public static String dateToString(Date d,String format){
		return FastDateFormat.getInstance(format).format(d);
	}

	/**
	 * 活动当前时间指定格式的字符串
	 * @param format
	 * @return
	 */
	public static String dateToString(String format){
		return FastDateFormat.getInstance(format).format(new Date());
	}
	
	/**
	 * 转换为网页显示格式
	 * @param d
	 * @return
	 */
	public static String toSimpleDateString(Date d) {
		if (d == null) {
			return null;
		}
		long old = d.getTime();
		long now = new Date().getTime();
		int m = (int) ((now - old) / DateUtils.MILLIS_PER_MINUTE);
		if (m < 60) {
			return (m == 0 ? 1 : m) + "分钟前";
		}
		int h = (int) ((now - old) / DateUtils.MILLIS_PER_HOUR);
		if (h < 24) {
			return h + "小时前";
		}
		int day = (int) ((now - old) / DateUtils.MILLIS_PER_DAY);
		if (day <= 3) {
			return day + "天前";
		}
		return dateToString(d,"yyyy-MM-dd HH:mm");
	}


	/*
     * 将时间转换为时间戳
     */
	public static String dateToStamp(String s) throws ParseException{
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = simpleDateFormat.parse(s);
		long ts = date.getTime();
		res = String.valueOf(ts);
		return res;
	}

	/*
     * 将时间戳转换为时间
     */
	public static String stampToDate(String s){
		String res;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long lt = new Long(s);
		Date date = new Date(lt);
		res = simpleDateFormat.format(date);
		return res;
	}
}
