package com.easipass.sys.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;

/**
 * 
 * @author mlzhang
 * @file StringUtil.java
 */
public class StringUtil {
	
	private static Log log = LogFactory.getLog(StringUtil.class);

	/**
	 * 截取一定长度，后补字符串...
	 * @param str
	 * @param len
	 * @param elide
	 * @return
	 */
	public static String splitString(String str, int len, String elide) {
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		
		str = str.replaceAll("<br>", " ");
		str = str.replaceAll("<br/>", " ");
		str = str.replaceAll("<br />", " ");
		str = str.replaceAll("<BR>", " ");
		str = str.replaceAll("<BR/>", " ");
		str = str.replaceAll("<BR />", " ");
		str = str.replaceAll("\n", " ");
		str = str.replaceAll("\r", " ");

		byte[] strByte;
		String resultStr = "";
		try {
			strByte = str.getBytes("UTF-8");
			int strLen = strByte.length;
			if (len >= strLen || len < 1) {
				return str;
			}

			int count = 0;
			for (int i = 0; i < len; i++) {
				int value = (int) strByte[i];
				if (value < 0) {
					count++;
				}
			}
			if (count % 3 == 1) {
				len = (len > 1) ? (len - 1) : 0;
			} else if (count % 3 == 2) {
				len = (len > 2) ? (len - 2) : 0;
			}

			resultStr = new String(strByte, 0, len, "UTF-8") + elide.trim();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(),e);
		}

		return resultStr;
	}
	
	
	/**
	 * 屏蔽使用HTML标签
	 * @param str
	 * @return
	 */
	public static String safeHTML(String str){
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll("\n", "<br>");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\"", "&quot;");
		
		return str;
	}
	
	public static String safeHTML2(String str){
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		
		str = str.replaceAll("&", "&amp;");
		str = str.replaceAll(">", "&gt;");
		str = str.replaceAll("<", "&lt;");
		str = str.replaceAll("\n", "");
		str = str.replaceAll("'", "&apos;");
		str = str.replaceAll("\"", "&quot;");
		
		return str;
	}
	
	/**
	 * 获取HTML内纯文本
	 * @param str
	 * @return
	 */
	public static String stripHTML(String str){
		if (StringUtils.isEmpty(str)) {
			return "";
		}
		
		str = str.replaceAll("<.+?>", "");
		str = str.replaceAll("<", "");
		str = str.replaceAll(">", "");
		str = str.replaceAll("&nbsp;", "");
        return str;
	}

}
