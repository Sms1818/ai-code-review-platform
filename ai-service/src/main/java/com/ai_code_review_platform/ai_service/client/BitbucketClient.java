package com.ai_code_review_platform.ai_service.client;

import java.time.Duration;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.ai_code_review_platform.ai_service.config.BitbucketProperties;
import com.ai_code_review_platform.ai_service.config.RetrySupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class BitbucketClient {

    private final WebClient webClient;
    private final BitbucketProperties properties;

    public void postComment(
            String workspace,
            String repositorySlug,
            Integer pullRequestId,
            String review) {

        String url = String.format(
                "%s/repositories/%s/%s/pullrequests/%d/comments",
                properties.getApiUrl(),
                workspace,
                repositorySlug,
                pullRequestId);

        Map<String, Object> requestBody = Map.of(
                "content",
                Map.of("raw", review));

        log.info(
                "Posting AI review comment to {}/{} PR #{}",
                workspace,
                repositorySlug,
                pullRequestId);

        webClient.post()
                .uri(url)
                .headers(headers -> headers.setBasicAuth(
                        properties.getUsername(),
                        properties.getPassword()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error(
                                            "Bitbucket API error (status={}): {}",
                                            response.statusCode(),
                                            errorBody);
                                    return Mono.error(
                                            new RuntimeException(
                                                    "Bitbucket API error: " + errorBody));
                                }))
                .toBodilessEntity()
                .retryWhen(RetrySupport.exponentialBackoff(
                        "Bitbucket postComment",
                        properties.getMaxRetries(),
                        Duration.ofSeconds(properties.getInitialBackoffSeconds()),
                        Duration.ofSeconds(properties.getMaxBackoffSeconds())))
                .timeout(Duration.ofSeconds(properties.getTimeoutSeconds()))
                .block();

        log.info(
                "Successfully posted AI review comment to {}/{} PR #{}",
                workspace,
                repositorySlug,
                pullRequestId);
    }
}
