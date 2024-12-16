package com.tripwhiz.tripwhizadminback.storeowner.controller;

import com.tripwhiz.tripwhizadminback.storeowner.entity.StoreOwner;
import com.tripwhiz.tripwhizadminback.storeowner.service.StoreOwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
@Log4j2
@RestController
@RequestMapping("/api/admin/manager")
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

}
