package com.easipass.sys.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Description: 获取IP工具类
 * @author: mlzhang
 * @date: 2016/10/17 16:56
 * @version: V1.0
 */
public class IpUtil {
    private static final Log log = LogFactory.getLog(IpUtil.class);

    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    public static boolean existsLocalIp(String localIp) {
        if(StringUtils.isEmpty(localIp)){
            return false;
        }
        Enumeration<NetworkInterface> allNetInterfaces;  //定义网络接口枚举类
        String ipStr = null;
        try {
            allNetInterfaces = NetworkInterface.getNetworkInterfaces();  //获得网络接口

            InetAddress ip = null; //声明一个InetAddress类型ip地址
            while (allNetInterfaces.hasMoreElements()) //遍历所有的网络接口
            {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                Enumeration<InetAddress> addresses = netInterface.getInetAddresses(); //同样再定义网络地址枚举类
                while (addresses.hasMoreElements())
                {
                    ip = addresses.nextElement();
                    if(localIp.equals(ip.getHostAddress())){
                        return true;
                    }
                }
            }
        } catch (SocketException e) {
            log.error("Error:",e);
        }
        return false;
    }
}
