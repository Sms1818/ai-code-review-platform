package com.ai_code_review_platform.auth_service.service;

import com.ai_code_review_platform.auth_service.dto.AuthResponse;
import com.ai_code_review_platform.auth_service.dto.LoginRequest;
import com.ai_code_review_platform.auth_service.dto.RegisterRequest;
import com.ai_code_review_platform.auth_service.entity.User;
import com.ai_code_review_platform.auth_service.exception.InvalidCredentialsException;
import com.ai_code_review_platform.auth_service.exception.UserAlreadyExistsException;
import com.ai_code_review_platform.auth_service.repository.UserRepository;
import com.ai_code_review_platform.auth_service.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final JwtUtil jwtUtil;

        public AuthResponse register(
                        RegisterRequest request) {

                if (userRepository.findByEmail(
                                request.getEmail()).isPresent()) {

                        throw new UserAlreadyExistsException(
                                        "Email already exists");
                }

                User user = User.builder()
                                .name(request.getName())
                                .email(request.getEmail())
                                .password(
                                                passwordEncoder.encode(
                                                                request.getPassword()))
                                .role("USER")
                                .build();

                userRepository.save(user);

                String token = jwtUtil.generateToken(
                                user.getEmail());

                return AuthResponse.builder()
                                .token(token)
                                .build();
        }

        public AuthResponse login(
                        LoginRequest request) {

                User user = userRepository.findByEmail(
                                request.getEmail()).orElseThrow(
                                                () -> new InvalidCredentialsException(
                                                                "Invalid credentials"));

                boolean matches = passwordEncoder.matches(
                                request.getPassword(),
                                user.getPassword());

                if (!matches) {
                        throw new RuntimeException(
                                        "Invalid credentials");
                }

                String token = jwtUtil.generateToken(
                                user.getEmail());

                return AuthResponse.builder()
                                .token(token)
                                .build();
        }
}