package com.ai_code_review_platform.review_service.service;

import com.ai_code_review_platform.review_service.dto.CodeReviewRequest;
import com.ai_code_review_platform.review_service.dto.CodeReviewResponse;
import com.ai_code_review_platform.review_service.entity.CodeReview;
import com.ai_code_review_platform.review_service.repository.CodeReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CodeReviewService {

    private final CodeReviewRepository repository;

    public CodeReviewResponse submitReview(
            CodeReviewRequest request) {

        CodeReview review = CodeReview.builder()
                .code(request.getCode())
                .language(request.getLanguage())
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(review);

        return CodeReviewResponse.builder()
                .reviewId(review.getId())
                .status(review.getStatus())
                .build();
    }
}