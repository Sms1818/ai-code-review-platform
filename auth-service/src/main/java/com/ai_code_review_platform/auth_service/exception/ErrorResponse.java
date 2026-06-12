package com.ai_code_review_platform.auth_service.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String message;

    private int status;

    private LocalDateTime timestamp;
}