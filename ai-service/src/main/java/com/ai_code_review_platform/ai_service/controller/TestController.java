package com.ai_code_review_platform.ai_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ai_code_review_platform.ai_service.client.GeminiClient;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final GeminiClient geminiClient;

    @GetMapping("/test")
    public String test() {
        return geminiClient.testConnection();
    }
}