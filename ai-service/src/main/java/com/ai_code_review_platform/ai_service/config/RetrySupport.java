package com.ai_code_review_platform.ai_service.config;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeoutException;

import org.springframework.web.reactive.function.client.WebClientResponseException;

import lombok.extern.slf4j.Slf4j;
import reactor.util.retry.Retry;

@Slf4j
public final class RetrySupport {

    private RetrySupport() {
    }

    public static Retry exponentialBackoff(
            String operation,
            int maxRetries,
            Duration initialBackoff,
            Duration maxBackoff) {

        return Retry.backoff(maxRetries, initialBackoff)
                .maxBackoff(maxBackoff)
                .filter(RetrySupport::isRetryable)
                .doBeforeRetry(signal -> log.warn(
                        "{} failed (attempt {}/{}): {}",
                        operation,
                        signal.totalRetries() + 1,
                        maxRetries,
                        signal.failure().getMessage()))
                .onRetryExhaustedThrow((spec, signal) -> {
                    log.error(
                            "{} failed after {} retries",
                            operation,
                            maxRetries,
                            signal.failure());
                    return signal.failure();
                });
    }

    private static boolean isRetryable(Throwable throwable) {
        if (throwable instanceof WebClientResponseException webClientException) {
            int status = webClientException.getStatusCode().value();
            return status >= 500 || status == 429;
        }

        return throwable instanceof TimeoutException
                || throwable instanceof IOException;
    }
}
