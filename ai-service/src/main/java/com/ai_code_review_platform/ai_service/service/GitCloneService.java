package com.ai_code_review_platform.ai_service.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.TransportConfigCallback;
import org.eclipse.jgit.transport.SshTransport;
import org.eclipse.jgit.transport.sshd.SshdSessionFactory;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class GitCloneService {

    public void cloneRepository(
            String repoUrl,
            String branchName) {

        try {

            String localPath = "repositories/" + branchName;

            File repoDir = new File(localPath);
            if (repoDir.exists()) {
                deleteDirectory(repoDir);
                log.info(
                        "Existing repository deleted");
            }

            SshdSessionFactory sshdSessionFactory = new SshdSessionFactory();

            TransportConfigCallback transportConfigCallback = transport -> {

                SshTransport sshTransport = (SshTransport) transport;

                sshTransport.setSshSessionFactory(
                        sshdSessionFactory);
            };

            Git git= Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(new File(localPath))
                    .setBranch(branchName)
                    .setTransportConfigCallback(
                            transportConfigCallback)
                    .call();

            git.fetch()
                .setTransportConfigCallback(
                    transportConfigCallback
                )
                .call();

            log.info(
                    "Repository cloned successfully at {}",
                    localPath);

        } catch (Exception e) {

            log.error(
                    "Error cloning repository",
                    e);
        }
    }

    private void deleteDirectory(File directory) {

        File[] files = directory.listFiles();

        if (files != null) {

            for (File file : files) {

                if (file.isDirectory()) {

                    deleteDirectory(file);

                } else {

                    file.delete();
                }
            }
        }

        directory.delete();
    }
}