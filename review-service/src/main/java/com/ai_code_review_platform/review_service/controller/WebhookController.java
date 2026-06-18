package com.ai_code_review_platform.review_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai_code_review_platform.review_service.dto.BitbucketWebhookRequest;
import com.ai_code_review_platform.review_service.service.WebhookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Slf4j
public class WebhookController {

        private final WebhookService webhookService;

        private final ObjectMapper objectMapper;

        @PostMapping("/bitbucket")
        public ResponseEntity<String> handleWebhook(
                        @RequestBody BitbucketWebhookRequest request) {

                try {

                        log.info(
                                        "RAW WEBHOOK:\n{}",
                                        objectMapper
                                                        .writerWithDefaultPrettyPrinter()
                                                        .writeValueAsString(request));

                } catch (Exception e) {

                        log.error(
                                        "Error logging webhook",
                                        e);
                }

                log.info("WEBHOOK HITTTTT 🔥");

                webhookService.processPullRequestEvent(
                                request);

                return ResponseEntity.ok(
                                "Webhook received successfully");
        }
}