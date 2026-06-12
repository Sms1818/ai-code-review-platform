package com.ai_code_review_platform.review_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "code_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CodeReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(columnDefinition = "TEXT")
    private String code;

    private String language;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String aiReview;

    private LocalDateTime createdAt;
}