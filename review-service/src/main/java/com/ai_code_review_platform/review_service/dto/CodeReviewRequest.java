package com.ai_code_review_platform.review_service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CodeReviewRequest {

    @NotBlank(message = "Code is required")
    private String code;

    @NotBlank(message = "Language is required")
    private String language;
}