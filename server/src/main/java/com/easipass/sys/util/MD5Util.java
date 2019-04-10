package com.easipass.sys.util;

import org.apache.commons.codec.digest.DigestUtils;

/**  
 * @Description: MD5工具类
 * @author EP-mlzhang
 * @date 2013年8月9日 下午1:30:56
 * @version V1.0  
 */
public class MD5Util {
    private static final String MD5_PREFIX = "";  //特征码
    private static final ThreadLocal local = new ThreadLocal();

    private MD5Util() {
        super();
    }

    public static MD5Util getEncrypt() {
        MD5Util encrypt = (MD5Util)local.get();
        if (encrypt == null) {
            encrypt = new MD5Util();
            local.set(encrypt);
        }
        return encrypt;
    }

    public static String encode(String s) {
        if (s == null) {
            return null;
        }
        return DigestUtils.md5Hex(MD5_PREFIX + s);
    }

    public static String encode(String prefix,String s,String suffix) {
        if (s == null) {
            return null;
        }
        return DigestUtils.md5Hex(prefix + s + suffix);
    }

    public static String encodeWithoutPrefix(String s) {
        if (s == null) {
            return null;
        }
        return DigestUtils.md5Hex(s);
    }

}
