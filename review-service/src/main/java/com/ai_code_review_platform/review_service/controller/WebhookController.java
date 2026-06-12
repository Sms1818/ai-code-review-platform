package com.ai_code_review_platform.review_service.controller;

import com.ai_code_review_platform.review_service.dto.BitbucketWebhookRequest;
import com.ai_code_review_platform.review_service.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

    private final WebhookService webhookService;

    @PostMapping("/bitbucket")
    public ResponseEntity<String> handleWebhook(
            @RequestBody BitbucketWebhookRequest request) {

        log.info("WEBHOOK HITTTTT 🔥");

        webhookService.processPullRequestEvent(
                request
        );

        return ResponseEntity.ok(
                "Webhook received successfully"
        );
    }
}