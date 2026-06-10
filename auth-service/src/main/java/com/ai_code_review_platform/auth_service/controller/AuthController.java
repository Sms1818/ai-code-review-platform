package com.ai_code_review_platform.auth_service.controller;

import com.ai_code_review_platform.auth_service.dto.AuthResponse;
import com.ai_code_review_platform.auth_service.dto.LoginRequest;
import com.ai_code_review_platform.auth_service.dto.RegisterRequest;
import com.ai_code_review_platform.auth_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(
            @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(
            @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}