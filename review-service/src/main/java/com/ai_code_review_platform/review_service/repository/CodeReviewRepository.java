package com.ai_code_review_platform.review_service.repository;

import com.ai_code_review_platform.review_service.entity.CodeReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeReviewRepository
        extends JpaRepository<CodeReview, String> {
}