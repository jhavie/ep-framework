package com.easipass.sys.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Method;

/**
 * Created by Merjiezo on 2017/1/10.
 */
public class ClassUtil {

    private static final Log log = LogFactory.getLog(ClassUtil.class);

    public static Class getClassByName(String packages, String className) throws ClassNotFoundException {
        return Class.forName(packages + className);
    }

    /**
     * 动态调用类方法
     * @param newClass
     * @param methodName
     * @return
     */
    public static Object getMethodResult(Class newClass, String methodName, Object[] args) {
        Method method = null;
        Object result = null;

        //获取需要调用的方法
        for(Method m: newClass.getDeclaredMethods()) {

            if(m.getName().equalsIgnoreCase(methodName)) {
                method = m;
                break;
            }
        }

        //调用方法
        try {
            result = method.invoke(newClass, args);
        } catch (Exception e) {
            log.error(e);
        }

        return result;
    }
}
