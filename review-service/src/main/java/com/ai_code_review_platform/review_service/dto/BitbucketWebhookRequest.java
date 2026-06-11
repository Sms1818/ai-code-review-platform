package com.ai_code_review_platform.review_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitbucketWebhookRequest {

    private String eventKey;

    private Repository repository;

    @JsonProperty("pullrequest")
    private PullRequest pullRequest;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Repository {

        private String name;

        private String fullName;

        private String cloneUrl;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PullRequest {

        private Integer id;

        private String title;

        private String description;

        private String sourceBranch;

        private String targetBranch;

        private Author author;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Author {

            private String username;

            private String displayName;

            private String emailAddress;
        }
    }
}