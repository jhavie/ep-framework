package com.easipass.business.util;

import java.util.ResourceBundle;

/**
 * 
 * @author gemstone
 *
 */
public class ConfUtil {
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("config");
	
	public static String getString(String key){
		return bundle.getString(key);
	}
	
	public static String[] getStrArray(String key){
		return bundle.getString(key).split(",");
	}
	
	public static Integer getInteger(String key){
		return Integer.parseInt(bundle.getString(key));
	}
	
	public static Long getLong(String key){
		return Long.parseLong(bundle.getString(key));
	}
	
	public static Integer[] getIntArray(String key){
		String[] arr = getStrArray(key);
		Integer[] int_arr = new Integer[arr.length];
		for(int i=0;i<int_arr.length;i++)
			int_arr[i] = Integer.parseInt(arr[i]);
		return int_arr;
	}
	
	
	public static void main(String[] v){
		System.out.println(getString("domain.url"));
	}
	
}
