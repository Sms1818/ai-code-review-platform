package com.ai_code_review_platform.auth_service.exception;

public class InvalidCredentialsException
        extends RuntimeException {

    public InvalidCredentialsException(
            String message) {

        super(message);
    }
}