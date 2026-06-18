package com.ai_code_review_platform.ai_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "gemini")
public class GeminiProperties {

    private String apiKey;

    private int timeoutSeconds = 60;

    private int maxRetries = 3;

    private int initialBackoffSeconds = 2;

    private int maxBackoffSeconds = 15;
}
