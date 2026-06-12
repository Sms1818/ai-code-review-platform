package com.ai_code_review_platform.review_service.controller;

import com.ai_code_review_platform.review_service.dto.CodeReviewRequest;
import com.ai_code_review_platform.review_service.dto.CodeReviewResponse;
import com.ai_code_review_platform.review_service.service.CodeReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class CodeReviewController {

    private final CodeReviewService reviewService;

    @PostMapping
    public CodeReviewResponse submitReview(
            @Valid @RequestBody CodeReviewRequest request) {

        return reviewService.submitReview(request);
    }
}