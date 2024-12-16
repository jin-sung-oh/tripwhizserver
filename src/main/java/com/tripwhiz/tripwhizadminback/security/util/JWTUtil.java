package com.tripwhiz.tripwhizadminback.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {

    private static final String SECRET_KEY = "1234567890123456789012345678901234567890123456789012345678901234"; // 256-bit key
    private static final String REFRESH_SECRET_KEY = "refreshSecretKey12345678901234567890123456789012345678901234567890"; // 256-bit refresh key

    public String createAccessToken(Map<String, Object> valueMap, int min) {
        SecretKey secretKey = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // 256-bit key
        try {
            // Add ROLE_ prefix for all roles
            valueMap = new HashMap<>(valueMap); // Clone map to avoid mutation
            String role = (String) valueMap.get("role");
            if (!role.startsWith("ROLE_")) {
                valueMap.put("role", "ROLE_" + role.toUpperCase());
            }

            return Jwts.builder()
                    .setClaims(valueMap)
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Unexpected error while creating JWT access token: {}", e.getMessage());
            throw new RuntimeException("Unexpected error while creating JWT access token", e);
        }
    }

    public String createRefreshToken(Map<String, Object> valueMap, int days) {
        SecretKey secretKey = Keys.hmacShaKeyFor(REFRESH_SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // 256-bit key
        try {
            // Add ROLE_ prefix for all roles
            valueMap = new HashMap<>(valueMap); // Clone map to avoid mutation
            String role = (String) valueMap.get("role");
            if (!role.startsWith("ROLE_")) {
                valueMap.put("role", "ROLE_" + role.toUpperCase());
            }

            return Jwts.builder()
                    .setClaims(valueMap)
                    .setIssuedAt(new Date())
                    .setExpiration(Date.from(ZonedDateTime.now().plusDays(days).toInstant()))
                    .signWith(secretKey, SignatureAlgorithm.HS256)
                    .compact();
        } catch (Exception e) {
            log.error("Unexpected error while creating JWT refresh token: {}", e.getMessage());
            throw new RuntimeException("Unexpected error while creating JWT refresh token", e);
        }
    }

    public Map<String, Object> validateToken(String token, boolean isRefreshToken) {
        String key = isRefreshToken ? REFRESH_SECRET_KEY : SECRET_KEY;
        SecretKey secretKey = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)); // 256-bit key
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            log.info("Validated Claims: {}", claims);
            return claims;
        } catch (JwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new RuntimeException("Invalid JWT token");
        }
    }
}