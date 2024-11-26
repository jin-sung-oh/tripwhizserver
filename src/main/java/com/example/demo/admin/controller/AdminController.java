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
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {

    private final AdminService adminService;
    private final StoreOwnerService storeOwnerService;

    // 어드민 회원가입
    @PostMapping("/register")
    public ResponseEntity<Admin> register(@RequestBody Admin admin) {
        if (admin.getId() == null || admin.getPw() == null || admin.getRole() == null) {
            log.info("Register endpoint hit");
            log.info("Admin registration payload: {}", admin);
            return ResponseEntity.badRequest().body(null);
        }
        Admin savedAdmin = adminService.save(admin); // save에서 비밀번호 암호화 처리
        return ResponseEntity.ok(savedAdmin);
    }

    // 점주 계정 생성
    @PostMapping("/createStoreOwner")
    @PreAuthorize("hasRole('ADMIN')")  // ADMIN 권한만 접근 허용
    public ResponseEntity<StoreOwner> createStoreOwner(@RequestBody StoreOwner storeOwner) {
        StoreOwner savedStoreOwner = storeOwnerService.save(storeOwner);
        return ResponseEntity.ok(savedStoreOwner);
    }

    // 점주 목록 조회
    @GetMapping("/storeOwners")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<StoreOwner>> getStoreOwners() {
        List<StoreOwner> storeOwners = storeOwnerService.findAll();
        return ResponseEntity.ok(storeOwners);
    }

    @DeleteMapping("/storeOwner/{sno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteStoreOwnerFromList(@PathVariable int sno) {
        log.info("Deleting store owner with sno: {}", sno);
        try {
            boolean isDeleted = storeOwnerService.delete(sno);
            if (isDeleted) {
                log.info("Store owner deleted successfully. sno: {}", sno);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            } else {
                log.warn("Store owner not found. sno: {}", sno);
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("점주를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting store owner: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 에러가 발생했습니다: " + e.getMessage());
        }
    }
}

