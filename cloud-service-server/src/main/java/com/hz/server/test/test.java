package com.hz.server.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class test {

    public static void main(String[] args) {
        try {

            String content = "测试使用字符串";

            File file = new File("./File/test1.txt");

            if(file.exists()){

                FileWriter fw = new FileWriter(file,false);

                BufferedWriter bw = new BufferedWriter(fw);

                bw.write(content);

                bw.close(); fw.close();

                System.out.println("test1 done!");

            }

        } catch (Exception e) {

            // TODO: handle exception

        }

    }
}
