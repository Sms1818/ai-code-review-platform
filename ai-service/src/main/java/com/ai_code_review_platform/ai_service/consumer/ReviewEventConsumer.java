package com.ai_code_review_platform.ai_service.consumer;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import com.ai_code_review_platform.ai_service.service.GitCloneService;
import com.ai_code_review_platform.ai_service.service.GitDiffService;
import com.ai_code_review_platform.ai_service.service.AIReviewService;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import com.ai_code_review_platform.ai_service.dto.PullRequestEventMessage;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewEventConsumer {

    private final GitCloneService gitCloneService;
    private final GitDiffService gitDiffService;
    private final AIReviewService aiReviewService;

    @KafkaListener(topics = "pr-review-events")
    public void consumeEvent(PullRequestEventMessage message) {
        log.info(
                "Received PR Event: {}",
                message);

        gitCloneService.cloneRepository(
                message.getCloneUrl(),
                message.getSourceBranch());

        String repoPath = "repositories/" + message.getSourceBranch();

        String diff = gitDiffService.generateDiff(
                repoPath, message.getTargetBranch());

        log.info(diff);

        String review = aiReviewService.generateReview(
                diff);

        log.info(
                "AI REVIEW:\n{}",
                review);
    }

}
