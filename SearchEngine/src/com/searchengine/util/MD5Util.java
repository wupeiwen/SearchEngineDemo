package com.searchengine.util;
import java.security.MessageDigest;
/**
 * MD5 16位加密工具类
 * @author BinG
 *
 */
public class MD5Util {  
    public static String MD5(String sourceStr) {  
        try {  
            // 获得MD5摘要算法的 MessageDigest对象  
            MessageDigest mdInst = MessageDigest.getInstance("MD5");  
            // 使用指定的字节更新摘要  
            mdInst.update(sourceStr.getBytes());  
            // 获得密文  
            byte[] md = mdInst.digest();  
            // 把密文转换成十六进制的字符串形式  
            StringBuffer buf = new StringBuffer();  
            for (int i = 0; i < md.length; i++) {  
                int tmp = md[i];  
                if (tmp < 0)  
                    tmp += 256;  
                if (tmp < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(tmp));  
            }  
            return buf.toString().substring(8, 24);// 16位加密  
            // return buf.toString();// 32位加密  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    } 
}