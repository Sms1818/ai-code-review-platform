package com.ai_code_review_platform.ai_service.dto.gemini;

import java.util.List;

import lombok.Data;

@Data
public class GeminiResponse {

    private List<Candidate> candidates;
}
