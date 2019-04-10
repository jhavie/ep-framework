package com.easipass.business.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

public class HttpUtils {

	private static final Log log = LogFactory.getLog(HttpUtils.class);
	
	public static final String CHARSET = "utf-8";
	public static final String PARAM = "str";
	
	public static String post(String url, String postStr)throws Exception {
		JSONObject json = new JSONObject();
		json.put(PARAM, postStr);
        RequestEntity re = new StringRequestEntity(json.toString(),"text/json",CHARSET);
		return post(url, re);
	}
	
	public static String post(String url, RequestEntity requestEntity)throws Exception {
		String responseString;
		PostMethod postMethod = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
        try{  
    		postMethod = new PostMethod(url);
        	postMethod.setRequestEntity(requestEntity);  
            int statusCode = getHttpClient().executeMethod(postMethod);  
            if(statusCode == HttpStatus.SC_OK){
                bis = new BufferedInputStream(postMethod.getResponseBodyAsStream());
                byte[] bytes = new byte[1024];  
                bos = new ByteArrayOutputStream();
                int count = 0;  
                while((count = bis.read(bytes))!= -1){  
                    bos.write(bytes, 0, count);  
                }  
                byte[] strByte = bos.toByteArray();  
                responseString = new String(strByte,0,strByte.length, HttpUtils.CHARSET);
            }else{
            	log.error(IOUtils.toString(postMethod.getResponseBodyAsStream(), HttpUtils.CHARSET));
            	throw new RuntimeException(" -- remote response failed error statusCode -- " + statusCode);
            } 
        }finally{  
        	if(bos != null)  bos.close();  
        	if(bis != null)  bis.close();  
        	if(postMethod != null) postMethod.releaseConnection();  
        }  
        
        return responseString;  
	}
	
	public static String post(String url, NameValuePair[] data)throws Exception {
		String responseString;
		PostMethod postMethod = null;
		BufferedInputStream bis = null;
		ByteArrayOutputStream bos = null;
        try{  
    		postMethod = new PostMethod(url);
        	postMethod.setRequestBody(data);
        	HttpClient client = new HttpClient();
            int statusCode = client.executeMethod(postMethod);  
            if(statusCode == HttpStatus.SC_OK){
                bis = new BufferedInputStream(postMethod.getResponseBodyAsStream());
                byte[] bytes = new byte[1024];  
                bos = new ByteArrayOutputStream();
                int count = 0;  
                while((count = bis.read(bytes))!= -1){  
                    bos.write(bytes, 0, count);  
                }  
                byte[] strByte = bos.toByteArray();  
                responseString = new String(strByte,0,strByte.length, HttpUtils.CHARSET);
            }else{
            	log.error(IOUtils.toString(postMethod.getResponseBodyAsStream(), HttpUtils.CHARSET));
            	throw new RuntimeException(" -- remote response failed error statusCode -- " + statusCode);
            } 
        }finally{  
        	if(bos != null)  bos.close();  
        	if(bis != null)  bis.close();  
        	if(postMethod != null) postMethod.releaseConnection();  
        }  
        
        return responseString;  
	}
	
	private static HttpClient getHttpClient(){
		HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true)); //连接在释放时即时关闭
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10*1000);//连接超时
    	client.getHttpConnectionManager().getParams().setSoTimeout(30*1000); //响应超时
		return client;
	}
	
	public static void main(String[] args) throws Exception {
		//System.out.println(post("http://localhost1/ecs/error_log_serv.jsp", ""));
		System.out.println(post("http://test.com", ""));
	}
}
