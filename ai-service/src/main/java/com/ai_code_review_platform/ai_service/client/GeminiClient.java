package com.ai_code_review_platform.ai_service.client;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiClient {

  private final WebClient webClient;

  @Value("${gemini.api-key}")
  private String apiKey;

  public String reviewCode(String diff) {

    String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
        + apiKey;

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

    return webClient.post()
        .uri(url)
        .bodyValue(requestBody)
        .retrieve()
        .onStatus(
            status -> status.isError(),
            response -> response.bodyToMono(String.class)
                .flatMap(errorBody -> {

                  log.error(
                      "Gemini Error Response: {}",
                      errorBody);

                  return Mono.error(
                      new RuntimeException(
                          errorBody));
                }))
        .bodyToMono(String.class)
        .block();
  }
}