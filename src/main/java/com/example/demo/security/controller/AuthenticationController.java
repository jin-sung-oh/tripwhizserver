package com.example.demo.security.controller;

import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.service.AdminService;
import com.example.demo.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AdminService adminService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // PasswordEncoder 주입

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String id = loginRequest.get("id");
        String pw = loginRequest.get("pw");

        // ID로 Admin 계정을 조회
        Admin admin = adminService.findById(id)
                .orElseThrow(() -> new RuntimeException("Invalid ID or Password"));

        // 비밀번호 검증 (BCryptPasswordEncoder 사용)
        if (!passwordEncoder.matches(pw, admin.getPw())) {
            return ResponseEntity.status(401).body(Map.of("msg", "Invalid ID or Password"));
        }

        // JWT 토큰 생성 (Access Token + Refresh Token)
        try {
            // AccessToken: 30분 유효, RefreshToken: 7일 유효
            String accessToken = jwtUtil.createAccessToken(Map.of("id", admin.getId(), "role", admin.getRole()), 30);
            String refreshToken = jwtUtil.createRefreshToken(Map.of("id", admin.getId(), "role", admin.getRole()), 7);  // 7일 유효

            return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("msg", "Token creation failed", "error", e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");

        try {
            // 리프레시 토큰 검증
            Map<String, Object> claims = jwtUtil.validateToken(refreshToken, true);
            String username = (String) claims.get("id");
            String role = (String) claims.get("role");

            // 새로운 Access Token 생성
            String newAccessToken = jwtUtil.createAccessToken(Map.of("id", username, "role", role), 30);  // Access Token 30분 유효
            return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("msg", "Invalid or expired refresh token"));
        }
    }
}
