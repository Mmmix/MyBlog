package com.wust.myblog.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class FtpUtil {
    private static String ftpIp = "39.108.84.248";
    private static String ftpUser = "mixy";
    private static String ftpPass = "mjx19971207";
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);

    private String ip;
    private int port;
    private String user;
    private String pwd;
    private FTPClient ftpClient;

    public FtpUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }

    public static boolean uploadFile(List<File> fileList) throws IOException {
        FtpUtil ftpUtil = new FtpUtil(ftpIp,21,ftpUser,ftpPass);
        boolean result = ftpUtil.uploadFile("img",fileList);
        logger.info("结束上传，上传结果:{}",result);
        return result;
    }

    private boolean uploadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        //连接ftp服务器
        if (connectServer(this.ip,this.port,this.user,this.pwd)){
            logger.info("连接ftp服务器成功");
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024*1024);
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.enterLocalPassiveMode();
                ftpClient.setDataTimeout(20000);
                ftpClient.setDefaultTimeout(20000);
                ftpClient.setConnectTimeout(20000);
                for (File fileItem:fileList) {
                    fis = new FileInputStream(fileItem);
                    InputStream in = new BufferedInputStream(fis);

                    logger.info("存储到ftp服务器,{}",fileItem.getName());
                    ftpClient.storeFile(fileItem.getName(),in);

                    logger.info(fileItem.getName()+"上传成功");
                }
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                e.printStackTrace();
                return false;
            } finally {
                fis.close();
                ftpClient.disconnect();
                logger.info("disconnected");
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip,int port,String user,String pwd){
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接服务器异常",e);
            e.printStackTrace();
        }
        return isSuccess;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
