package com.easipass.business.util;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by mlzhang on 2016/9/28.
 */
public class ECIQUtil {
    public static byte[] encrypt(byte[] param) {
        if (param == null || param.length == 0) {
            return null;
        }
        byte[] result = new byte[param.length * 2];
        for (int i = 0; i < param.length; ++i) {
            result[(2 * i)] = (byte) (param[i] >> 4 & 0x0F | 0x60);
            result[(2 * i + 1)] = (byte) (param[i] & 0x0F | 0x40);
        }
        //颠倒
        byte b;
        for (int i = 0; i < result.length / 2; i++) {
            b = result[result.length - 1 - i];
            result[result.length - 1 - i] = result[i];
            result[i] = b;
        }

        return result;
    }

    public static String ecodingPasswd(String src, String method) {

        try {
            // 信息摘要算法
            MessageDigest md5 = MessageDigest.getInstance(method);
            md5.update(src.getBytes());
            byte[] encoding = md5.digest();
            // 使用64进行编码，一避免出现丢数据情景
            return new String(Base64.encodeBase64(encoding));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e + "加密失败！！");
        }

    }


    public static void main(String[] args) {
        byte[] b2 = null;
        try {
            b2 = "000000".getBytes("GBK");
            byte[] result2 = encrypt(b2);
            System.out.println(new String(result2, "GBK"));
            String encodeSoftType = ecodingPasswd("B34F22E90A30003B5A865A86AD65E8D1600030254", "md5");
            System.out.println(encodeSoftType);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 解压byte数组类型的Zip
     *
     * @param data
     * @return
     * @throws IOException
     */
    private static byte[] unZip(byte[] data) throws IOException {
        byte[] b = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ZipInputStream zip = new ZipInputStream(bis);
        while (zip.getNextEntry() != null) {
            byte[] buf = new byte[8096];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = zip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
        }
        zip.close();
        bis.close();
        return b;
    }

    public static byte[] unZip2(byte[] data) throws IOException {
        byte[] b = null;
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ZipInputStream zip = new ZipInputStream(bis);
        while (zip.getNextEntry() != null) {
            byte[] buf = new byte[8096];
            int num = -1;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((num = zip.read(buf, 0, buf.length)) != -1) {
                baos.write(buf, 0, num);
            }
            b = baos.toByteArray();
            baos.flush();
            baos.close();
        }
        zip.close();
        bis.close();
        return b;
    }

    public static byte[] zip(byte[] data) throws IOException {
        byte[] b = null;
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ZipOutputStream gos = new ZipOutputStream(baos);

        int count;
        byte data2[] = new byte[1024];

        gos.putNextEntry(new ZipEntry("compress"));

            while ((count = bais.read(data2, 0, 1024)) != -1) {
                gos.write(data2, 0, count);
            }
            gos.finish();

            gos.flush();
            gos.close();

            byte[] output = baos.toByteArray();

            baos.flush();
            baos.close();

            bais.close();


        return output;
    }

    public static List<String> returnMsg(List<String> xmlBody) throws IOException {
        List<String> xmlBodyMsgs = new ArrayList<>();
        for (String msg : xmlBody) {
            byte[] bodyDecoded = unZip(Base64.decodeBase64(msg.getBytes()));
            String newXmlMsg = new String(bodyDecoded, "GBK");
            xmlBodyMsgs.add(newXmlMsg);
        }
        return xmlBodyMsgs;
    }
}
