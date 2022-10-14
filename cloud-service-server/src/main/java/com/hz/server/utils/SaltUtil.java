package com.hz.server.utils;

import org.apache.shiro.crypto.hash.Md5Hash;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SaltUtil {
    public static final String KEY_SHA = "SHA";
    public static final String ra="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 字符串sha加密
     * @param s 要加密的字符串
     * @return 加密后的字符串
     */
    public static String sha(String s)
    {
        BigInteger sha =null;
        byte[] bys = s.getBytes();
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_SHA);
            messageDigest.update(bys);
            sha = new BigInteger(messageDigest.digest());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sha.toString(32);
    }

    public static String shiroSha(String s,String salt)
    {
        Md5Hash md5Hash = new Md5Hash(s,salt,1024);
        return md5Hash.toHex();
    }

    /**
     * 字符串+随机盐 sha加密
     * @param s 要加密的字符串
     * @return 盐和加密后的字符串
     */
    public static Map<String,String> getResult(String s){
        Map<String,String> map=new HashMap<String,String>();
        String salt=getSalt();
        map.put("salt", salt);//盐
        map.put("password", sha(s+salt));//加密后的密码
        return map;
    }

    public static Map<String,String>  shiroSalt(String s){
        Map<String,String> map=new HashMap<String,String>();
        Md5Hash md5Hash = new Md5Hash(s,"123",1024);
        System.out.println(md5Hash.toHex());
        String salt=getSalt();
        map.put("salt", "123");//盐
        map.put("password", md5Hash.toHex());//加密后的密码
        return map;
        //return md5Hash.toHex();
    }

    /**
     * 生成随机盐
     * @return 随机生成的盐
     */
    public static String getSalt() {
        SecureRandom random=new SecureRandom();
        int length=random.nextInt(5)+8;//盐的长度，这里是8-12可自行调整
        char[] text = new char[length];
        for (int i = 0; i < length; i++) {
            text[i] = ra.charAt(random.nextInt(ra.length()));
        }
        return new String(text);
    }
}
