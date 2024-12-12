package com.example.demo.security.controller;

import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.service.AdminService;
import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.service.StoreOwnerService;
import com.example.demo.security.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthenticationController {

    private final AdminService adminService;
    private final StoreOwnerService storeOwnerService;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String id = loginRequest.get("id");
        String pw = loginRequest.get("pw");
        String role = loginRequest.get("role");

        if (id == null || id.isBlank() || pw == null || pw.isBlank() || role == null || role.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("msg", "id, pw, role은 필수 입력값입니다."));
        }

        if ("ADMIN".equalsIgnoreCase(role)) {
            return processAdminLogin(id, pw);
        } else if ("STOREOWNER".equalsIgnoreCase(role)) {
            return processStoreOwnerLogin(id, pw);
        } else {
            return ResponseEntity.badRequest().body(Map.of("msg", "유효하지 않은 역할(role)입니다."));
        }
    }

    private ResponseEntity<?> processAdminLogin(String id, String pw) {
        Admin admin = adminService.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 관리자 ID입니다."));

        if (!passwordEncoder.matches(pw, admin.getPw())) {
            return ResponseEntity.status(401).body(Map.of("msg", "비밀번호가 일치하지 않습니다."));
        }

        return generateTokens(admin.getId(), admin.getRole());
    }

    private ResponseEntity<?> processStoreOwnerLogin(String id, String pw) {
        StoreOwner storeOwner = storeOwnerService.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 점주 ID입니다."));

        if (!passwordEncoder.matches(pw, storeOwner.getPw())) {
            return ResponseEntity.status(401).body(Map.of("msg", "비밀번호가 일치하지 않습니다."));
        }

        return generateTokens(storeOwner.getId(), "STOREOWNER");
    }

    private ResponseEntity<?> generateTokens(String id, String role) {
        String accessToken = jwtUtil.createAccessToken(Map.of("id", id, "role", role), 60);
        String refreshToken = jwtUtil.createRefreshToken(Map.of("id", id, "role", role), 7);
        return ResponseEntity.ok(Map.of("accessToken", accessToken, "refreshToken", refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");
        Map<String, Object> claims = jwtUtil.validateToken(refreshToken, true);
        String newAccessToken = jwtUtil.createAccessToken(Map.of("id", claims.get("id"), "role", claims.get("role")), 30);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody Map<String, String> tokenRequest) {
        String refreshToken = tokenRequest.get("refreshToken");

        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("msg", "refreshToken은 필수 입력값입니다."));
        }

        // 검증 단계: 유효한 리프레시 토큰인지 확인
        try {
            jwtUtil.validateToken(refreshToken, true);

            // 블랙리스트 저장 또는 데이터베이스에서 삭제 로직 (옵션)
            log.info("로그아웃 처리된 리프레시 토큰: {}", refreshToken);

            return ResponseEntity.ok(Map.of("msg", "로그아웃되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("msg", "유효하지 않은 리프레시 토큰입니다."));
        }
    }

}
