package com.sftp;

import org.apache.sshd.common.file.virtualfs.VirtualFileSystemFactory;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.server.auth.UserAuthFactory;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.UserAuthPasswordFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.scp.ScpCommandFactory;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.subsystem.SubsystemFactory;
import org.apache.sshd.server.subsystem.sftp.SftpSubsystemFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SftpServerStarter {

    private static final Logger logger = LoggerFactory.getLogger(SftpServerStarter.class);

    public static void main(String[] args) {
        try {
            logger.info("Start Server...");
            Path path = Paths.get("H:\\Temp\\sftp");
            SshServer sshServer = SshServer.setUpDefaultServer();
            sshServer.setPort(22);
            sshServer.setKeyPairProvider(new SimpleGeneratorHostKeyProvider(Paths.get("H:\\Temp\\sftp\\hostkey.ser")));
            sshServer.setCommandFactory(new ScpCommandFactory());

            List<UserAuthFactory> userAuthFactories = new ArrayList<>();
            userAuthFactories.add(new UserAuthPasswordFactory());
            sshServer.setUserAuthFactories(userAuthFactories);

            sshServer.setFileSystemFactory(new VirtualFileSystemFactory(path));
            sshServer.setPasswordAuthenticator(new PasswordAuthenticator() {
                @Override
                public boolean authenticate(String username, String password, ServerSession session) {
                    if ((username.equals("test")) && (password.equals("test"))) {
                        //sshServer.setFileSystemFactory(new VirtualFileSystemFactory(path));
                        return true;
                    }
                    if ((username.equals("admin")) && (password.equals("admin"))) {
                        //sshServer.setFileSystemFactory(new VirtualFileSystemFactory(path));
                        return true;
                    }
                    return false;
                }
            });
            List<SubsystemFactory> namedFactoryList = new ArrayList<>();
            namedFactoryList.add(new SftpSubsystemFactory());
            sshServer.setSubsystemFactories(namedFactoryList);
            sshServer.start();
            while(true);
        } catch (IOException ex) {
            logger.error("", ex);
        }
    }
}
