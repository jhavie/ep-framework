package com.easipass.sys.db;

import com.easipass.sys.util.DateFormatUtil;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Query工厂类工具类
@Slf4j
public class QueryUtils {

    // 把驼峰字符串换成下划线字符串
    public static String replaceWithLine (String s) {
        String[] strings = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        for (String string : strings) {
            s = s.replace(string,"_"+string);
        }
        return s.toUpperCase();
    }

    public static Object changeStringToDate (Object o , String betweenEndFlag) {
        Calendar c = Calendar.getInstance();
        if (o instanceof String) {
            try {
                String format = DateFormatUtil.stringToFormat(o.toString());
                if ("yyyy-MM-dd HH:mm:ss".equals(format)) {
                    return new SimpleDateFormat(format).parse(o.toString());
                } else if ("yyyy-MM-dd".equals(format)) {
                    Date sDate = new SimpleDateFormat(format).parse(o.toString());
                    c.setTime(sDate);
                    if ("1".equals(betweenEndFlag)) {
                        c.add(Calendar.DAY_OF_MONTH, 1);
                    }
                    return c.getTime();
                } else {
                    log.error("日期格式不匹配");
                    return null;
                }
            } catch (ParseException pe) {
                log.error("日期转换失败");
                return null;
            }
        }else {
            return o;
        }
    }

    public static Object[] objectArrayChange(Object o) {
        if (o instanceof int[]) {
            int[] ints = (int[])o;
            Object[] objs = new Object[ints.length];
            for (int i = 0; i < ints.length; i++) {
                objs[i] = ints[i];
            }
            return objs;
        } else if(o instanceof String[]) {
            String[] strings = (String[])o;
            Object[] objs = new Object[strings.length];
            for (int i = 0; i < strings.length; i++) {
                objs[i] = "'" + strings[i] + "'";
            }
            return objs;
        } else if(o instanceof List) {
            List list2 = new ArrayList();
            Iterator it = ((List)o).iterator();
            while(it.hasNext()){
                Object obj = it.next();
                if(obj instanceof String){
                    list2.add("'" + obj + "'");
                }
            }
            return list2.toArray();
        }
        return ((List) o).toArray();
    }
}
