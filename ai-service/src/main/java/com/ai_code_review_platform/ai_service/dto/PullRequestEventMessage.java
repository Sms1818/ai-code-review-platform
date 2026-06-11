package com.ai_code_review_platform.ai_service.dto;

import lombok.Data;

@Data
public class PullRequestEventMessage {

    private Integer pullRequestId;

    private String title;

    private String repositoryName;

    private String sourceBranch;

    private String targetBranch;

    private String author;

    private String cloneUrl;
}