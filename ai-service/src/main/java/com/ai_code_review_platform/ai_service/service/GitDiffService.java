package com.ai_code_review_platform.ai_service.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GitDiffService {

    public String generateDiff(String repoPath, String targetBranch) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "git",
                    "diff",
                    "origin/" + targetBranch);

            processBuilder.directory(new File(repoPath));

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            StringBuilder diff = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                diff.append(line).append("\n");
            }

            process.waitFor();
            log.info(
                    "Git diff generated successfully");

            return diff.toString();

        } catch (Exception e) {
            log.error(
                    "Error generating git diff",
                    e);

            return "";
        }
    }
}
