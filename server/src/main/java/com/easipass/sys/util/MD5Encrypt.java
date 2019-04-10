package com.easipass.sys.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @Description: MD5工具类
 * @author EP-mlzhang
 * @date 2013年8月9日 下午1:30:56
 * @version V1.0
 */
public final class MD5Encrypt {
    private static final String MD5_PREFIX = "http://edi.easipass.com";  //特征码
    private static final String MD5_SUFFIX = "ecs.easipass.com";//密钥
    private static final ThreadLocal local = new ThreadLocal();

    private MD5Encrypt() {
        super();
    }

    public static MD5Encrypt getEncrypt() {
        MD5Encrypt encrypt = (MD5Encrypt)local.get();
        if (encrypt == null) {
            encrypt = new MD5Encrypt();
            local.set(encrypt);
        }
        return encrypt;
    }

    public static String encodeWithPrefix(String s) {
        if (s == null) {
            return null;
        }
        return DigestUtils.md5Hex(MD5_PREFIX + s);
    }

    public static String encodeWithSuffix(String s) {
        if (s == null) {
            return null;
        }
        return DigestUtils.md5Hex(s + MD5_SUFFIX);
    }

    public static String encodeWithoutfix(String s) {
        if (s == null) {
            return null;
        }
        return DigestUtils.md5Hex(s);
    }

    public static void main(String[] args){
        String passwd =  "mobile3212";
        System.out.println(passwd + "  ,MD5Encrypt： " + encodeWithPrefix(passwd)) ;
    }
}
