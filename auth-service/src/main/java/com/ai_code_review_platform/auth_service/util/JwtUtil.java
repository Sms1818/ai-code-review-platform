package com.ai_code_review_platform.auth_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

        private final String SECRET = "mysecretkeymysecretkeymysecretkey";

        private final SecretKey key = Keys.hmacShaKeyFor(
                        SECRET.getBytes(StandardCharsets.UTF_8));

        public String generateToken(String email) {

                return Jwts.builder()
                                .subject(email)
                                .issuedAt(new Date())
                                .expiration(
                                                new Date(
                                                                System.currentTimeMillis()
                                                                                + 86400000))
                                .signWith(key)
                                .compact();
        }

        public String extractEmail(String token) {

                return extractClaims(token)
                                .getSubject();
        }

        public boolean validateToken(
                        String token,
                        String email) {

                try {

                        String extractedEmail = extractEmail(token);

                        return extractedEmail.equals(email)
                                        && !isTokenExpired(token);

                } catch (Exception e) {

                        return false;
                }
        }

        private boolean isTokenExpired(
                        String token) {

                return extractClaims(token)
                                .getExpiration()
                                .before(new Date());
        }

        private Claims extractClaims(
                        String token) {

                return Jwts.parser()
                                .verifyWith(key)
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();
        }
}