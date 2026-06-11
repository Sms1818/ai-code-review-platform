package com.ai_code_review_platform.ai_service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ai_code_review_platform.ai_service.dto.PullRequestEventMessage;

@Service
@Slf4j
public class ReviewEventConsumer {

    @KafkaListener(topics = "pr-review-events", groupId = "ai-review-group")
    public void consumeEvent(PullRequestEventMessage message) {
        log.info(
                "Received PR Event: {}",
                message);

        log.info(
                "Repository: {}",
                message.getRepositoryName());

        log.info(
                "PR Title: {}",
                message.getTitle());
    }

}
