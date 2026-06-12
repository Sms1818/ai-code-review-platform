package com.ai_code_review_platform.review_service.producer;

import com.ai_code_review_platform.review_service.config.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.ai_code_review_platform.review_service.dto.PullRequestEventMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewEventProducer {
    private final KafkaTemplate<String, PullRequestEventMessage> kafkaTemplate;

    public void publishReviewEvent(PullRequestEventMessage message) {
        kafkaTemplate.send(KafkaTopics.PR_REVIEW_TOPIC, message);

        log.info("Kafka Event Published: {}", message);
    }
}
