package com.ai_code_review_platform.review_service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeReviewResponse {

    private String reviewId;

    private String status;
}