package com.ai_code_review_platform.ai_service.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GitDiffService {

    public String generateDiff(
            String repoPath,
            String targetBranch) {

        try {

            File repoDirectory = new File(repoPath);

            ProcessBuilder branchBuilder = new ProcessBuilder(
                    "git",
                    "branch",
                    "--show-current");

            branchBuilder.directory(repoDirectory);

            Process branchProcess = branchBuilder.start();

            BufferedReader branchReader = new BufferedReader(
                    new InputStreamReader(
                            branchProcess.getInputStream()));

            String currentBranch = branchReader.readLine();

            log.info(
                    "Current Branch = {}",
                    currentBranch);

            // Get merge base
            ProcessBuilder mergeBaseBuilder = new ProcessBuilder(
                    "git",
                    "merge-base",
                    "HEAD",
                    "origin/" + targetBranch);

            mergeBaseBuilder.directory(repoDirectory);

            Process mergeBaseProcess = mergeBaseBuilder.start();

            BufferedReader mergeBaseReader = new BufferedReader(
                    new InputStreamReader(
                            mergeBaseProcess.getInputStream()));

            String mergeBaseCommit = mergeBaseReader.readLine();

            mergeBaseProcess.waitFor();

            log.info(
                    "Merge Base Commit = {}",
                    mergeBaseCommit);

            // Generate actual PR diff
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "git",
                    "diff",
                    mergeBaseCommit,
                    "HEAD");

            processBuilder.directory(
                    repoDirectory);

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            process.getInputStream()));

            StringBuilder diff = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {

                diff.append(line)
                        .append("\n");
            }

            int exitCode = process.waitFor();

            log.info(
                    "Git diff generated successfully");

            log.info(
                    "Git diff exit code = {}",
                    exitCode);

            log.info(
                    "Diff length = {}",
                    diff.length());

            if (!diff.isEmpty()) {

                log.info(
                        "First 1000 chars of diff:\n{}",
                        diff.substring(
                                0,
                                Math.min(
                                        1000,
                                        diff.length())));
            }

            return diff.toString();

        } catch (Exception e) {

            log.error(
                    "Error generating git diff",
                    e);

            return "";
        }
    }
}