package com.ai_code_review_platform.review_service.repository;

import com.ai_code_review_platform.review_service.entity.PullRequestEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PullRequestEventRepository
        extends JpaRepository<PullRequestEvent, String> {
}