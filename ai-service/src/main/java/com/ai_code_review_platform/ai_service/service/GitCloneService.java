package com.ai_code_review_platform.ai_service.service;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
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
                    .setCredentialsProvider(
                            new UsernamePasswordCredentialsProvider(
                                    "sms1818-v",
                                    "ATATT3xFfGF0opW-DZr_ckaXh0ZcbUi8BUbrpoWK-1uJ_Pv3NGBEruOaTFhE_w1uKbq-16hDvIjpTY32MoMmQHZQ4Xvt0oNcHuch0QJ12FnS-m0iaEuU-DjnzGKbx-SmPXupTzN-YD4WMT63mCiiGy5zzMDHYy5GcurtAjbsU6FdQtVsoT1kZQk=69CE6E44"
                            )
                    )
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