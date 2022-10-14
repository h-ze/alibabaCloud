package com.hz.server.utils;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class UrlUtils {
    private static final Logger logger = LoggerFactory.getLogger(UrlUtils.class);
    public final static int OUT_CWS = 0;
    public final static int OUT_CLOUD = 1;
    public final static int OUT_PLATFORM = 2;
    public final static int OUT_CMIS = 3;
    public final static int OUT_CAS = 4;
    public final static int OUT_ONLINE = 5;
    public final static int OUT_APPSTORE = 6;
    public final static int OUT_WEBPDF = 7;
    public final static int OUT_NAGIOS = 8;
    public final static int OUT_CPDFADMIN = 9;
    //public final static int OUT_FINAL = 10;
    public final static int OUT_MQTT =10;

    public final static String URLPREFIX[] = {"cws", "cloud", "platform", "cmis", "cas", "online", "appstore", "webpdf", "nagios", "cpdfadmin","emqtt"};

    public static String changeURL(String server, int outOptions){
        if (outOptions < 0 ||
                outOptions > OUT_MQTT)
            return null;

        URL url = null;
        try {
            url = new URL(server);
        } catch (MalformedURLException e) {
            logger.error(e.toString());
        }
        if (url == null)
            return null;

        String sHost = url.getHost();

        String[] split = sHost.split("\\.");

        if (split.length == 0)
            return null;

        if (server.contains("online")){
            split[0] = split[0].replace("online",URLPREFIX[outOptions]);
        }else if (server.contains("cws")){
            split[0] = split[0].replace("online",URLPREFIX[outOptions]);
        }
        String sRet = new String();

        for (int i = 0; i < split.length; i++){
            sRet += split[i] + ".";
        }

        sRet = url.getProtocol() + "://" + sRet.substring(0, sRet.length() - 1);

        return sRet;
    }

}
