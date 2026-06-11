package com.ai_code_review_platform.auth_service.exception;

public class UserAlreadyExistsException
        extends RuntimeException {

    public UserAlreadyExistsException(
            String message) {

        super(message);
    }
}