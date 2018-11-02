package com.wust.myblog.common;

public enum FtpEnum {
    ftpEnum("39.108.84.248","mixy","pwd","http://image.mixy.top/");

    private String ip;
    private String user;
    private String pwd;
    private  String prefixUrl;

    FtpEnum(String ip, String user, String pwd, String prefixUrl) {
        this.ip = ip;
        this.user = user;
        this.pwd = pwd;
        this.prefixUrl = prefixUrl;
    }

    public String getIp() {
        return ip;
    }

    public String getUser() {
        return user;
    }

    public String getPwd() {
        return pwd;
    }

    public String getPrefixUrl() {
        return prefixUrl;
    }
}
