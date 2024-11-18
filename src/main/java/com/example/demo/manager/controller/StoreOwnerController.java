package com.example.demo.manager.controller;

import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.service.StoreOwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class StoreOwnerController {

    private final StoreOwnerService storeOwnerService;

    // 점주 생성
    @PostMapping("/createStoreOwner")
    @PreAuthorize("hasRole('ADMIN')") // 관리자 권한 확인
    public ResponseEntity<StoreOwner> createStoreOwner(@RequestBody StoreOwner storeOwner) {
        StoreOwner savedStoreOwner = storeOwnerService.save(storeOwner);
        return ResponseEntity.ok(savedStoreOwner);
    }

    // 점주 상세 조회
    @GetMapping("/storeOwner/{s_no}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')") // 관리자와 매니저 권한 확인
    public ResponseEntity<StoreOwner> getStoreOwner(@PathVariable String s_no) {
        return storeOwnerService.findByS_no(s_no)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
