package com.ai_code_review_platform.ai_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ai_code_review_platform.ai_service.dto.PullRequestEventMessage;
import com.ai_code_review_platform.ai_service.service.AIReviewService;
import com.ai_code_review_platform.ai_service.service.GitCloneService;
import com.ai_code_review_platform.ai_service.service.GitDiffService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ReviewEventConsumer {

        private final GitCloneService gitCloneService;
        private final GitDiffService gitDiffService;
        private final AIReviewService aiReviewService;

        @KafkaListener(topics = "pr-review-events")
        public void consumeEvent(PullRequestEventMessage message) {
                try {
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
                } catch (Exception e) {
                        log.error("FAILED", e);
                        throw e;
                }
        }

}
