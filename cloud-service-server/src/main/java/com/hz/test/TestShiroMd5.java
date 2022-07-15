package com.hz.test;

import org.apache.shiro.crypto.hash.Md5Hash;

/**
 * 对密码进行加密
 */
public class TestShiroMd5 {
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIyIiwic3ViIjoidGVzdCIsImlhdCI6MTYxNjQ3ODI1MywiZXhwIjoxNjE2NTY0NjUzLCJwYXNzd29yZCI6IjEyMzQ1NiIsInJvbGVzIjoiMTIzIn0.SJ2zW7ONA6KC2l4JcnvqIMYMdLHU399jRF9aTdTfEiw","",1024);
        System.out.println(md5Hash.toHex());
    }
}
