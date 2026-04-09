package com.careerportal.career_backend.security;

import com.careerportal.career_backend.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final Duration tokenTtl;

    public JwtService(
            @Value("${app.jwt.secret}") String rawSecret,
            @Value("${app.jwt.expiration-minutes:120}") long expirationMinutes) {
        this.signingKey = toSigningKey(rawSecret);
        this.tokenTtl = Duration.ofMinutes(expirationMinutes);
    }

    public String generateToken(User user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(tokenTtl);
        String role = user.getRole() == null ? "STUDENT" : user.getRole().toUpperCase();

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claims(Map.of(
                        "email", user.getEmail(),
                        "name", user.getName(),
                        "role", role))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Instant calculateExpirationInstant() {
        return Instant.now().plus(tokenTtl);
    }

    private SecretKey toSigningKey(String rawSecret) {
        if (rawSecret == null || rawSecret.isBlank()) {
            throw new IllegalArgumentException("app.jwt.secret must be configured");
        }

        String trimmed = rawSecret.trim();

        try {
            return Keys.hmacShaKeyFor(Decoders.BASE64.decode(trimmed));
        } catch (Exception ignored) {
            byte[] bytes = trimmed.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(bytes);
        }
    }
}
