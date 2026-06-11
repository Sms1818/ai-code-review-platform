package com.ai_code_review_platform.review_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PullRequestEventMessage {

    private Integer pullRequestId;

    private String title;

    private String repositoryName;

    private String sourceBranch;

    private String targetBranch;

    private String author;

    private String cloneUrl;
}