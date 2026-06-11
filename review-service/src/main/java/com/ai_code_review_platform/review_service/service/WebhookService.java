package com.ai_code_review_platform.review_service.service;

import com.ai_code_review_platform.review_service.dto.BitbucketWebhookRequest;
import com.ai_code_review_platform.review_service.entity.PullRequestEvent;
import com.ai_code_review_platform.review_service.producer.ReviewEventProducer;
import com.ai_code_review_platform.review_service.repository.PullRequestEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebhookService {

    private final PullRequestEventRepository repository;
    private final ReviewEventProducer producer;

    public void processPullRequestEvent(
            BitbucketWebhookRequest request) {

        PullRequestEvent event = PullRequestEvent.builder()
                .eventKey(request.getEventKey())
                .repositoryName(
                        request.getRepository()
                                .getName())
                .pullRequestId(
                        request.getPullRequest()
                                .getId())
                .title(
                        request.getPullRequest()
                                .getTitle())
                .author(
                        request.getPullRequest()
                                .getAuthor()
                                .getDisplayName())
                .sourceBranch(
                        request.getPullRequest()
                                .getSourceBranch())
                .targetBranch(
                        request.getPullRequest()
                                .getTargetBranch())
                .status("RECEIVED")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(event);

        producer.publishReviewEvent("PR Created: " + event.getTitle());

        log.info(
                "Pull Request Event Saved Successfully: {}",
                event.getId());
    }
}