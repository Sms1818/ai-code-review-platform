package com.ai_code_review_platform.ai_service.client;

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

  public String testConnection() {
    String url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
        + apiKey;

    String requestBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "Say hello in one sentence"
                }
              ]
            }
          ]
        }
        """;

    String response = webClient.post()
        .uri(url)
        .header("Content-Type", "application/json")
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(String.class)
        .block();

    return response;
  }

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

        Git Diff:

        %s
        """.formatted(diff);

    String requestBody = """
        {
          "contents": [
            {
              "parts": [
                {
                  "text": "%s"
                }
              ]
            }
          ]
        }
        """.formatted(
        prompt.replace("\"", "\\\"")
            .replace("\n", "\\n"));

    return webClient.post()
        .uri(url)
        .header("Content-Type", "application/json")
        .bodyValue(requestBody)
        .retrieve()
        .onStatus(
            status -> status.isError(),
            response -> response.bodyToMono(String.class)
                .flatMap(errorBody -> {

                  log.error("Gemini Error Response: {}", errorBody);

                  return Mono.error(
                      new RuntimeException(errorBody));
                }))
        .bodyToMono(String.class)
        .block();
  }
}
