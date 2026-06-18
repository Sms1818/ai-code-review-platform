package com.ai_code_review_platform.ai_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ai_code_review_platform.ai_service.client.BitbucketClient;
import com.ai_code_review_platform.ai_service.dto.PullRequestEventMessage;
import com.ai_code_review_platform.ai_service.service.AIReviewService;
import com.ai_code_review_platform.ai_service.service.GitCloneService;
import com.ai_code_review_platform.ai_service.service.GitDiffService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewEventConsumer {

        private final GitCloneService gitCloneService;
        private final GitDiffService gitDiffService;
        private final AIReviewService aiReviewService;
        private final BitbucketClient bitbucketClient;

        @KafkaListener(topics = "pr-review-events")
        public void consumeEvent(PullRequestEventMessage message) {
                log.info(
                                "Received PR Event: PR #{} in {}/{}",
                                message.getPullRequestId(),
                                message.getWorkspace(),
                                message.getRepositorySlug());

                try {
                        gitCloneService.cloneRepository(
                                        message.getCloneUrl(),
                                        message.getSourceBranch());

                        String repoPath = "repositories/" + message.getSourceBranch();

                        String diff = gitDiffService.generateDiff(
                                        repoPath, message.getTargetBranch());

                        String review = aiReviewService.generateReview(diff);

                        log.info(
                                        "AI review generated for PR #{} ({} chars)",
                                        message.getPullRequestId(),
                                        review.length());

                        bitbucketClient.postComment(
                                        message.getWorkspace(),
                                        message.getRepositorySlug(),
                                        message.getPullRequestId(),
                                        review);

                        log.info(
                                        "AI review posted to Bitbucket PR #{} in {}/{}",
                                        message.getPullRequestId(),
                                        message.getWorkspace(),
                                        message.getRepositorySlug());
                } catch (Exception e) {
                        log.error(
                                        "Failed to process PR #{} in {}/{}: {}",
                                        message.getPullRequestId(),
                                        message.getWorkspace(),
                                        message.getRepositorySlug(),
                                        e.getMessage(),
                                        e);
                        throw e;
                }
        }
}
