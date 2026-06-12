package com.ai_code_review_platform.ai_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AIReviewService {

    public String generateReview(
            String gitDiff) {

        log.info(
                "Generating AI review...");

        if (gitDiff == null || gitDiff.isBlank()) {

            return "No code changes detected.";
        }

        return """
                AI Review Summary:

                - Code structure looks clean
                - Consider adding null checks
                - Add proper exception handling
                - Improve method naming consistency
                - Add unit tests for new changes
                """;
    }
}