package com.ai_code_review_platform.review_service.controller;

import com.ai_code_review_platform.review_service.dto.BitbucketWebhookRequest;
import com.ai_code_review_platform.review_service.service.WebhookService;
import lombok.RequiredArgsConstructor;
import com.ai_code_review_platform.review_service.producer.ReviewEventProducer;
import com.ai_code_review_platform.review_service.dto.PullRequestEventMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
public class WebhookController {

    private final WebhookService webhookService;
    private final ReviewEventProducer producer;

    @PostMapping("/bitbucket")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload) {

        System.out.println(payload);
        

        return ResponseEntity.ok(
                "Webhook received successfully");
    }

    @GetMapping("/test-kafka")
    public ResponseEntity<String> testKafka() {

        producer.publishReviewEvent(

                PullRequestEventMessage.builder()
                        .pullRequestId(1)
                        .title("Test PR")
                        .repositoryName("ai-code-review")
                        .sourceBranch("feature/test")
                        .targetBranch("main")
                        .author("Sahil")
                        .cloneUrl("dummy-url")
                        .build());

        return ResponseEntity.ok(
                "Kafka test event published");
    }
}