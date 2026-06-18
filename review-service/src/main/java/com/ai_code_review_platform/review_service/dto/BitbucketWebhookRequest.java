package com.ai_code_review_platform.review_service.dto;

import java.util.List;

import lombok.Data;

@Data
public class BitbucketWebhookRequest {

    private Repository repository;

    private PullRequest pullrequest;

    private Actor actor;

    @Data
    public static class Repository {

        private String name;

        private String full_name;

        private Links links;
    }

    @Data
    public static class Links {

        private List<CloneLink> clone;
    }

    @Data
    public static class CloneLink {

        private String name;

        private String href;
    }

    @Data
    public static class PullRequest {

        private Integer id;

        private String title;

        private Source source;

        private Destination destination;
    }

    @Data
    public static class Source {

        private Branch branch;
    }

    @Data
    public static class Destination {

        private Branch branch;
    }

    @Data
    public static class Branch {

        private String name;
    }

    @Data
    public static class Actor {

        private String display_name;
    }
}