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
        @RequestBody BitbucketWebhookRequest request){

    webhookService.processPullRequestEvent(
            request
    );

    return ResponseEntity.ok(
            "Webhook received successfully"
    );
}

}