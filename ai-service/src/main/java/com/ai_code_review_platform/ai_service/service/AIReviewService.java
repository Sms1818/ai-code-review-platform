package com.ai_code_review_platform.ai_service.service;

import org.springframework.stereotype.Service;

import com.ai_code_review_platform.ai_service.client.GeminiClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AIReviewService {
    private final GeminiClient geminiClient;

    public String generateReview(
            String gitDiff) {

        log.info(
                "Generating AI review...");

        if (gitDiff == null || gitDiff.isBlank()) {

            return "No code changes detected.";
        }

        return geminiClient.reviewCode(gitDiff);
    }
}