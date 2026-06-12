package com.ai_code_review_platform.review_service.service;

import com.ai_code_review_platform.review_service.dto.BitbucketWebhookRequest;
import com.ai_code_review_platform.review_service.dto.PullRequestEventMessage;
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

    if (request.getPullrequest() == null) {

        log.warn(
                "Pull Request data not found in webhook payload");

        return;
    }

    PullRequestEvent event = PullRequestEvent.builder()
            .eventKey("pullrequest:created")
            .repositoryName(
                    request.getRepository()
                            .getName())
            .pullRequestId(
                    request.getPullrequest()
                            .getId())
            .title(
                    request.getPullrequest()
                            .getTitle())
            .author(
                    request.getActor()
                            .getDisplay_name())
            .sourceBranch(
                    request.getPullrequest()
                            .getSource()
                            .getBranch()
                            .getName())
            .targetBranch(
                    request.getPullrequest()
                            .getDestination()
                            .getBranch()
                            .getName())
            .status("RECEIVED")
            .createdAt(LocalDateTime.now())
            .build();

    repository.save(event);

    PullRequestEventMessage message =
            PullRequestEventMessage.builder()
                    .pullRequestId(
                            request.getPullrequest()
                                    .getId())
                    .title(
                            request.getPullrequest()
                                    .getTitle())
                    .repositoryName(
                            request.getRepository()
                                    .getName())
                    .sourceBranch(
                            request.getPullrequest()
                                    .getSource()
                                    .getBranch()
                                    .getName())
                    .targetBranch(
                            request.getPullrequest()
                                    .getDestination()
                                    .getBranch()
                                    .getName())
                    .author(
                            request.getActor()
                                    .getDisplay_name())
                    .cloneUrl(
                            "https://bitbucket.org/sms1818-v/ai-code-review.git")
                    .build();

    producer.publishReviewEvent(message);

    log.info(
            "Pull Request Event Saved Successfully: {}",
            event.getId());
}
}