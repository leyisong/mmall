package com.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * 连接ftp服务器的util类
 */
public class FTPUtil {

    private static Logger logger= LoggerFactory.getLogger(FTPUtil.class);
    private static  String ftpIp=PropertiesUtil.getProperty("ftp.server.ip");
    private static  String ftpUser=PropertiesUtil.getProperty("ftp.user");
    private static  String ftpPass=PropertiesUtil.getProperty("ftp.pass");

    public FTPUtil(String ip, int port, String user, String pwd) {
        this.ip = ip;
        this.port = port;
        this.user = user;
        this.pwd = pwd;
    }
    //上传
    public  static  boolean uploadFile(List<File> files) throws IOException {
        FTPUtil ftpUtil=new FTPUtil(ftpIp,21,ftpUser,ftpPass);
        logger.info("开始连接FTP服务器");
        boolean result = ftpUtil.uploadFile("img",files);
        logger.info("开始连接FTP服务器...结束上传,上传的结果:{}",result);
    return result;
    }
    //上传 的具体逻辑封装起来
    //remotePath服务器上的具体文件夹
    private boolean uploadFile(String remotePath,List<File> files) throws IOException {
        boolean uploaded=true;
        FileInputStream fis=null;
        //连接ftp服务器
        if (connectService(this.ip,this.port,this.user,this.pwd)) {
            try {
                //更换工作文件夹
                ftpClient.changeWorkingDirectory(remotePath);
                    //设置缓冲区
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                //遍历上传
                for (File fileItem:files){
                    fis=new FileInputStream(fileItem);
                    ftpClient.storeFile(fileItem.getName(),fis);
                }
            } catch (IOException e) {
                logger.error("上传文件异常",e);
                uploaded=false;
            }finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }
    //连接ftp服务器
    private boolean connectService(String ip, int port, String user, String pwd){
        ftpClient=new FTPClient();
        boolean isSuccess=false;
        try {
            ftpClient.connect(ip);
            isSuccess= ftpClient.login(user,pwd);
        } catch (IOException e) {
            logger.error("连接FTP服务器异常",e);
        }
        return isSuccess;
    }








    private  String ip;//ip地址
    private  int port;//端口号
    private  String user;
    private  String pwd;

    private FTPClient ftpClient;

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

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
