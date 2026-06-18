package com.ai_code_review_platform.ai_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "bitbucket")
public class BitbucketProperties {

    private String apiUrl = "https://api.bitbucket.org/2.0";

    private String username;

    private String password;

    private int timeoutSeconds = 30;

    private int maxRetries = 3;

    private int initialBackoffSeconds = 1;

    private int maxBackoffSeconds = 10;
}
