package com.common.entity;


/**
 * @author Admin
 * @create 2019-04-15 10:51
 **/

public class Server {
    private String server;
    private String proxy;
    private String port;
    private String proxyUser;
    private String proxyPass;
    private boolean trust;

    public boolean isTrust() {
        return trust;
    }

    public Server() {
    }

    public Server(String server) {
        this.server = server;
    }

    public Server(String server, String proxy, String port, String proxyUser, String proxyPass) {
        this.server = server;
        this.proxy = proxy;
        this.port = port;
        this.proxyUser = proxyUser;
        this.proxyPass = proxyPass;
    }

    public String getServer() {
        return server;
    }

    public String getServer(int outOptions) {

        //return UrlUtils.changeURL(server, outOptions);
        return "";
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }

    public String getProxyPass() {
        return proxyPass;
    }

    public void setProxyPass(String proxyPass) {
        this.proxyPass = proxyPass;
    }

    public void setServer(String server) {
        this.server = server;
    }

    @Override
    public String toString() {
        return "Server{" +
                "server='" + server + '\'' +
                ", proxy='" + proxy + '\'' +
                ", port='" + port + '\'' +
                ", proxyUser='" + proxyUser + '\'' +
                ", proxyPass='" + proxyPass + '\'' +
                '}';
    }
}
