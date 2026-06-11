package com.ai_code_review_platform.auth_service.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public String currentUser(
            Authentication authentication) {

        return "Authenticated User: "
                + authentication.getName();
    }
}