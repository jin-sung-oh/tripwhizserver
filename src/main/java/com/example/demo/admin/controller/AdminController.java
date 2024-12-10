package com.example.demo.admin.controller;

import com.example.demo.admin.entity.Admin;
import com.example.demo.admin.service.AdminService;
import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.service.StoreOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final AdminService adminService;
    private final StoreOwnerService storeOwnerService;

    // 어드민 회원가입
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Admin admin) {
        if (admin.getId() == null || admin.getPw() == null || admin.getRole() == null) {
            log.warn("Admin registration failed: Missing required fields");
            return ResponseEntity.badRequest().body("필수 값이 누락되었습니다.");
        }

        try {
            Admin savedAdmin = adminService.save(admin);
            log.info("Admin registered successfully: {}", savedAdmin.getId());
            return ResponseEntity.ok(savedAdmin);
        } catch (RuntimeException e) {
            log.error("Admin registration failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID가 이미 존재합니다.");
        }
    }

    // 점주 계정 생성
    @PostMapping("/createStoreOwner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createStoreOwner(@RequestBody StoreOwner storeOwner) {
        if (storeOwner.getId() == null || storeOwner.getPw() == null || storeOwner.getRole() == null) {
            log.warn("StoreOwner creation failed: Missing required fields");
            return ResponseEntity.badRequest().body("필수 값이 누락되었습니다.");
        }

        try {
            StoreOwner savedStoreOwner = storeOwnerService.save(storeOwner);
            log.info("StoreOwner created successfully: {}", savedStoreOwner.getId());
            return ResponseEntity.ok(savedStoreOwner);
        } catch (RuntimeException e) {
            log.error("StoreOwner creation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("ID가 이미 존재합니다.");
        }
    }

    // 점주 목록 조회
    @GetMapping("/storeOwners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StoreOwner>> getStoreOwners() {
        List<StoreOwner> storeOwners = storeOwnerService.findAll();
        log.info("Fetched store owner list: {}", storeOwners.size());
        return ResponseEntity.ok(storeOwners);
    }

    // 점주 삭제
    @DeleteMapping("/storeOwner/{sno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStoreOwner(@PathVariable Long sno) {
        log.info("Attempting to delete store owner with sno: {}", sno);

        try {
            boolean isDeleted = storeOwnerService.delete(sno);
            if (isDeleted) {
                log.info("Store owner deleted successfully: {}", sno);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                log.warn("Store owner not found: {}", sno);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("점주를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting store owner: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 에러가 발생했습니다: " + e.getMessage());
        }
    }
}
