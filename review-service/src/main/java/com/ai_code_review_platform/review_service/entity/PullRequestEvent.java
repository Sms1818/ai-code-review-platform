package com.ai_code_review_platform.review_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "pull_request_events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PullRequestEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String eventKey;

    private String repositoryName;

    private Integer pullRequestId;

    private String title;

    private String author;

    private String sourceBranch;

    private String targetBranch;

    private String status;

    private LocalDateTime createdAt;
}