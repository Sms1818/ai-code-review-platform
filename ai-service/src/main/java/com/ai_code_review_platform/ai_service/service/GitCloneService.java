package com.ai_code_review_platform.ai_service.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@Slf4j
public class GitCloneService {

    public void cloneRepository(
            String repoUrl,
            String branchName
    ) {

        try {

            String localPath =
                    "repositories/" + branchName;

            Git.cloneRepository()
                    .setURI(repoUrl)
                    .setDirectory(new File(localPath))
                    .setBranch(branchName)
                    .call();

            log.info(
                    "Repository cloned successfully at {}",
                    localPath
            );

        } catch (Exception e) {

            log.error(
                    "Error cloning repository",
                    e
            );
        }
    }
}