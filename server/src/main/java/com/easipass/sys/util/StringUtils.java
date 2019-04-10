package com.easipass.sys.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * ClassName: StringUtils
 * </p>
 * <p>
 * Description: String处理工具类
 * </p>
 * <p>
 * Author: colick
 * </p>
 * <p>
 * Date: 2015-05-15
 * </p>
 */
public class StringUtils extends org.springframework.util.StringUtils {

    public static final String BLANK = "";

    public static final String DEFAULT_CHARSET = "utf-8";

    public static final String DOT = ".";

    public static String firstCharUpperCase(String src) {
        if (Character.isUpperCase(src.charAt(0)))
            return src;
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toUpperCase(src.charAt(0))).append(
                    src.substring(1));
            return sb.toString();
        }
    }

    public static String[] match(String src, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(src);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                l.add(m.group(i));
            }
        }
        return l.toArray(new String[]{});
    }

    public static String matchGroup(String src, String reg, int index) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(src);
//		List<String> l = new ArrayList<String>();
        while (m.find()) {
            return m.group(index);
        }
        return BLANK;
    }

    public static String[] matchAll(String src, String reg) {
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(src);
        List<String> l = new ArrayList<String>();
        while (m.find()) {
            l.add(m.group());
        }
        return l.toArray(new String[]{});
    }

    public static String stringtify(Reader in) {
        try {
            StringBuilder sb = new StringBuilder();
            String line = null;
            BufferedReader br = new BufferedReader(in);
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public static String stringtify(InputStream in) {
        return stringtify(in, DEFAULT_CHARSET);
    }

    public static String stringtify(InputStream in, String charset) {
        try {
            InputStreamReader isr = new InputStreamReader(in, charset);
            return stringtify(isr);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * 字符串前补零
     *
     * @param src       源
     * @param padLength 字符格式化位数
     * @return 返回补零后的结果
     */
    public static String lpad(String src, Integer padLength) {
        int length = padLength - src.length();
        for (int i = 0; i < length; i++) {
            src = "0" + src;
        }
        return src;
    }

    /**
     * 比较是否含有给定字符串
     *
     * @param src 源
     * @param reg 正则
     * @return
     */
    public static boolean matches(String src, String reg) {
        return Pattern.compile(reg).matcher(src).find();
    }

    /**
     * 过滤字符串中的换行符和制表符
     *
     * @param str
     * @return
     */
//    public static String replaceBlank(String str) {
//        String dest = BLANK;
//        if (Utils.notNull(str)) {
//            Pattern p = Pattern.compile("\t|\r|\n");
//            Matcher m = p.matcher(str);
//            dest = m.replaceAll(BLANK);
//        }
//        return dest;
//    }

    public static boolean isBlank(String str) {
        int strLen;
        if(str != null && (strLen = str.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if(!Character.isWhitespace(str.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    //随机生成6位验证码
    public static String genRandomPsd(int length) {
        Random rad=new Random();
        String result  = rad.nextInt(1000000) +"";
        if(result.length()!=length){
            return genRandomPsd(length);
        }
        return result;
    }

    public static String valueOf(Object obj) {
        if (obj != null) {
            return obj.toString();
        }
        return "";
    }

    public static boolean isEmpty(Object str) {
        if (str == null || valueOf(str).length() == 0) {
            return true;
        } else {
            return false;
        }
    }

}
