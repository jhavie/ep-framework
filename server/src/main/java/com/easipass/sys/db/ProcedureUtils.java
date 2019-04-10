package com.easipass.sys.db;

import com.easipass.sys.constant.SysConstants;
import com.easipass.sys.exception.EasiServiceException;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

// 存储过程工厂类方法
@Slf4j
public class ProcedureUtils {

    public static Map<Integer,Object> excute (DataSource dataSource,
                                              String procedureName, Object[] inParams, int[] outParams) throws Exception {
        Connection conn = null;
        CallableStatement cs = null;
        Map<Integer,Object> procedureResultMap = new HashMap<>();
        try {
            conn = dataSource.getConnection();
            //调用存储过程
            cs = conn.prepareCall(procedureName);
            int i = 0;
            //通过反射动态执行cs的set方法
            for (; i < inParams.length ; i++) {
                Class<?> c = Class.forName("java.sql.CallableStatement");
                String type = ProcedureUtils.packageChange(inParams[i]).getSimpleName();
                Method method=c.getMethod("set"+type.substring(0,1).toUpperCase() + type.substring(1), int.class, ProcedureUtils.packageChange(inParams[i]));
                method.invoke(cs, i+1,inParams[i]);
            }
            for(int j = 0; j < outParams.length ; j++){
                cs.registerOutParameter(j + i + 1, outParams[j]);
            }
            cs.executeUpdate();
            for(int j = 0; j < outParams.length ; j++){
                Class<?> c = Class.forName("java.sql.CallableStatement");
                String type = ProcedureUtils.typeChange(outParams[j]);
                Method method=c.getMethod("get"+type, int.class);
                Object result = method.invoke(cs, j + i + 1);
                procedureResultMap.put(j + i + 1,result);
            }

        } catch (Exception e) {
            log.error(e.getMessage() , e);
            throw new EasiServiceException(SysConstants.ERROR_CODE005,e.getMessage());
        } finally {
            if (cs != null) cs.close();
            if (conn != null) conn.close();
        }
        return procedureResultMap;
    }

    private static Class packageChange (Object o) {
        if(o instanceof Integer){
            return int.class;
        }else if(o instanceof Long){
            return long.class;
        }else if(o instanceof Byte){
            return byte.class;
        }else if(o instanceof Short){
            return short.class;
        }else if(o instanceof Float){
            return float.class;
        }else if(o instanceof Double){
            return double.class;
        }else if(o instanceof Character){
            return char.class;
        }else if(o instanceof Boolean){
            return boolean.class;
        }else {
            return o.getClass();
        }
    }

    private static String typeChange (int type) {
        switch (type){
            case 4:
                return "Long";
            case 6:
                return "Float";
            case 8:
                return "Double";
            case 12:
                return "String";
            case 91:
                return "Date";
            default:
                return "String";
        }
    }
}
