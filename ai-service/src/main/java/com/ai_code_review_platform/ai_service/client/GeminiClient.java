package com.ai_code_review_platform.ai_service.client;

import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ai_code_review_platform.ai_service.config.GeminiProperties;
import com.ai_code_review_platform.ai_service.config.RetrySupport;
import com.ai_code_review_platform.ai_service.dto.gemini.Candidate;
import com.ai_code_review_platform.ai_service.dto.gemini.GeminiResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiClient {

    private final WebClient webClient;
    private final GeminiProperties properties;

    public String reviewCode(String diff) {

        String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                + properties.getApiKey();

        log.info("Diff length = {}", diff.length());

        String prompt = """
                You are a senior software engineer.

                Review the following git diff.

                Focus on:
                - Bugs
                - Performance issues
                - Security concerns
                - Code quality
                - Best practices

                For each issue provide:
                1. Severity (HIGH/MEDIUM/LOW)
                2. Explanation
                3. Suggested Fix

                Git Diff:

                %s
                """.formatted(diff);

        Map<String, Object> requestBody = Map.of(
                "contents",
                List.of(
                        Map.of(
                                "parts",
                                List.of(
                                        Map.of(
                                                "text",
                                                prompt)))));

        GeminiResponse response = webClient.post()
                .uri(url)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error(
                                            "Gemini Error Response: {}",
                                            errorBody);
                                    return Mono.error(
                                            new RuntimeException(
                                                    errorBody));
                                }))
                .bodyToMono(GeminiResponse.class)
                .retryWhen(RetrySupport.exponentialBackoff(
                        "Gemini generateContent",
                        properties.getMaxRetries(),
                        Duration.ofSeconds(properties.getInitialBackoffSeconds()),
                        Duration.ofSeconds(properties.getMaxBackoffSeconds())))
                .timeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                .block();

        return extractReviewText(response);
    }

    public String testConnection() {
        return reviewCode("diff --git a/Test.java b/Test.java\n+// test");
    }

    private String extractReviewText(GeminiResponse response) {
        if (response == null
                || response.getCandidates() == null
                || response.getCandidates().isEmpty()) {
            throw new RuntimeException("Gemini returned empty response");
        }

        Candidate candidate = response.getCandidates().get(0);
        if (candidate.getContent() == null
                || candidate.getContent().getParts() == null
                || candidate.getContent().getParts().isEmpty()) {
            throw new RuntimeException("Gemini returned no content");
        }

        String text = candidate.getContent().getParts().get(0).getText();
        if (text == null || text.isBlank()) {
            throw new RuntimeException("Gemini returned empty review text");
        }

        return text;
    }
}
