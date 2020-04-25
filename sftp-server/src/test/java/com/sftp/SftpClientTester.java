package com.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SftpClientTester {

    private static final Logger logger = LoggerFactory.getLogger(SftpClientTester.class);

    private SftpConfig sftpConfig;

    private SftpChannel sftpChannel;

    private ChannelSftp sftp = null;

    @Before
    public void setUp() {
        sftpConfig = new SftpConfig();
        sftpConfig.setHost("127.0.0.1");
        sftpConfig.setPort(22);
        sftpConfig.setUsername("test");
        sftpConfig.setPwd("test");

        sftpChannel = new SftpChannel();
        try {
            if (StringUtils.isNotBlank(sftpConfig.getPrivateKeyPath())) {
                sftp = sftpChannel.connectByIdentity(sftpConfig);
            } else {
                sftp = sftpChannel.connectByPwd(sftpConfig);
            }
            if (sftp.isConnected()) {
                logger.info("连接服务器成功");
            } else {
                logger.error("连接服务器失败");
            }

        } catch (Exception e) {
            logger.error("连接服务器失败", e);
        }

    }

    @Test
    public void testDownload() {
        download("/", "ftpserver.vsd", "H:\\Temp\\SftpClient\\download", null);
    }

    @Test
    public void testMkdir() {
        mkdir("/test/dd/ff/ee");
    }

    /**sftp 下载 */
    public boolean download(String baseDir, String fileName1, String filePath, String fileName2 ) {
        try {
            if (isConnected()) {
                String dst;
                if (StringUtils.isBlank(fileName2)) {
                    dst = filePath + "/" + fileName1;
                } else{
                    dst = filePath + "/" + fileName2;
                }
                if (baseDir == null || "/".equals(baseDir)) {
                    baseDir = "";
                }
                String src = baseDir + "/" + fileName1;
                logger.info("开始下载，sftp服务器路径：["+src +"], 目标服务器路径：["+dst+"]");
                sftp.get(src, dst);
                logger.info("下载成功");
                return true;
            }
        } catch (Exception e) {
            logger.error("下载失败", e);
            return false;
        } finally {
           closed();
        }
        return false;
    }

    public void mkdir(String path) {
        if (isConnected()) {
            List<String>  dirs = Arrays.asList(path.split("/")).stream().filter(s -> StringUtils.isNotBlank(s)).collect(Collectors.toList());
            dirs.forEach(dir -> {
                boolean isFile;
                String pwd = "/";
                try {
                    pwd = sftp.pwd();
                    sftp.cd(dir);
                    isFile = true;
                } catch (SftpException e) {
                    isFile = false;
                }
                if (!isFile) {
                    try {
                        sftp.mkdir(dir);
                        sftp.cd(dir);
                    } catch (SftpException e) {
                        logger.error("当前目录：" + pwd);
                        logger.error("创建文件夹失败[" + dir + "]：", e);
                    }
                }
            });
            logger.info("当前目录：" + path);
        }
    }

    private boolean isConnected() {
        if (sftp != null && sftp.isConnected()) {
            return true;
        }
        return false;
    }

    private void closed() {
        if (sftp != null) {
            sftp.disconnect();
        }
        if(sftpChannel != null) {
            sftpChannel.closeChannel();
        }
    }
}
