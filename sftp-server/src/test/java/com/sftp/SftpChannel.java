package com.sftp;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SftpChannel {

    Session session = null;
    Channel channel = null;

    //端口默认为22
    public static final int SFTP_DEFAULT_PORT = 22;

    /** 利用JSch包实现SFTP下载、上传文件(秘钥方式登陆)*/
    public ChannelSftp connectByIdentity(SftpConfig sftpConfig) throws JSchException {
        JSch jsch = new JSch();
        int port = SFTP_DEFAULT_PORT;
        //设置密钥和密码
        //支持密钥的方式登陆，只需在jsch.getSession之前设置一下密钥的相关信息就可以了
        if (StringUtils.isNotBlank(sftpConfig.getPrivateKeyPath())) {
            if (StringUtils.isNotBlank(sftpConfig.getPassphrase())) {
                //设置带口令的密钥
                jsch.addIdentity(sftpConfig.getPrivateKeyPath(), sftpConfig.getPassphrase());
            } else {
                //设置不带口令的密钥
                jsch.addIdentity(sftpConfig.getPrivateKeyPath());
            }
        }
        if (sftpConfig.getPort() != null) {
            port = Integer.valueOf(sftpConfig.getPort());
        }
        if (port > 0) {
            //采用指定的端口连接服务器
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), port);
        } else {
            //连接服务器，采用默认端口
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost());
        }
        if (session == null) {
            throw new JSchException("session为空，连接失败");
        }
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setTimeout(30000);

        session.connect();
        //创建sftp通信通道
        channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public ChannelSftp connectByPwd(SftpConfig sftpConfig) throws JSchException, FileNotFoundException {
        JSch jsch = new JSch();

        //设置hostkey
        String knownHostPublicKey = "127.0.0.1 ssh-rsa BAAAB3NzaC1yc2EAAAADAQABAAABAQDC+2YGEQni06TfmJgLLhEyZ7ucaUOleGJoA/BWCdtVW/5HWGCnieymlfX6mFvkco+muIGlMvAaLexICLg8ELnEWssiIu9RruBfkkmg3pDTtarO3mup/Il1U1EgT04zsmzlMieVyNvcJ9fuvwJAbzHtd5ydiJ3kND7gEd0Ry7Rt/FIca/kp9ZdojFwO4qGHQsGfa7EzwldDRIyTYTPoCHxNWi257ODrpgD4jUFk5Q/uHE9q3DZ6cZ15KvMoYueqKS2kir/3jMP41qAaq1mD41RrCIThqFUrHbLs/VvvqUgOXl1no80C1zK5t623i1BgTmSv5PAAkOJJG4HKjugkkc8J";
        jsch.setKnownHosts(new ByteArrayInputStream(knownHostPublicKey.getBytes()));

        int port = SFTP_DEFAULT_PORT;
        if (sftpConfig.getPort() != null) {
            port = Integer.valueOf(sftpConfig.getPort());
        }
        if (port > 0) {
            //采用指定的端口连接服务器
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost(), port);
        } else {
            //连接服务器，采用默认端口
            session = jsch.getSession(sftpConfig.getUsername(), sftpConfig.getHost());
        }
        if (session == null) {
            throw new JSchException("session为空，连接失败");
        }
        //设置登陆主机的密码
        session.setPassword(sftpConfig.getPwd());//设置密码
        Properties sshConfig = new Properties();
        sshConfig.put("StrictHostKeyChecking", "no");
        session.setConfig(sshConfig);
        session.setTimeout(30000);

        session.connect();
        //创建sftp通信通道
        channel = session.openChannel("sftp");
        channel.connect();
        return (ChannelSftp) channel;
    }

    public void closeChannel() {
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
        }
    }
}
