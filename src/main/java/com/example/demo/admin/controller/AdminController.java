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
    public ResponseEntity<List<StoreOwner>> getStoreOwners() {
        List<StoreOwner> storeOwners = storeOwnerService.findAll();
        return ResponseEntity.ok(storeOwners);
    }

    // 점주 정보 조회
    @GetMapping("/storeOwner/{s_no}")
    public ResponseEntity<StoreOwner> getStoreOwner(@PathVariable String s_no) {
        return storeOwnerService.findByS_no(s_no)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 점주 계정 삭제
    @DeleteMapping("/deleteStoreOwner/{s_no}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteStoreOwner(@PathVariable String s_no) {
        storeOwnerService.delete(s_no);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();  // No content 반환
    }

    // 관리자 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdmin(@PathVariable String id) {
        return adminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}