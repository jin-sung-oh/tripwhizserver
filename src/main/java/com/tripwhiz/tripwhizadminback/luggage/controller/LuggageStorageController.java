package com.tripwhiz.tripwhizadminback.luggage.controller;

import com.tripwhiz.tripwhizadminback.luggage.dto.LuggageStorageDTO;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorageStatus;
import com.tripwhiz.tripwhizadminback.luggage.service.LuggageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storeowner/luggagestorage")
@RequiredArgsConstructor
public class LuggageStorageController {

    private final LuggageStorageService luggageStorageService;

    @PostMapping("/create")
    public ResponseEntity<Void> createLuggageStorage(@RequestBody LuggageStorage luggageStorage) {
        luggageStorageService.processLuggageStorage(luggageStorage);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{lsno}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<LuggageStorageDTO> getLuggageStorageDetails(@PathVariable Long lsno) {
        LuggageStorageDTO luggageStorageDTO = luggageStorageService.getLuggageStorageDetails(lsno);
        return ResponseEntity.ok(luggageStorageDTO);
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<List<LuggageStorageDTO>> getAllLuggageStorages() {
        List<LuggageStorageDTO> luggageStorages = luggageStorageService.getAllLuggageStorages();
        return ResponseEntity.ok(luggageStorages);
    }

    @PutMapping("/{lsno}/status")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<Void> updateStorageStatus(@PathVariable Long lsno, @RequestParam LuggageStorageStatus status) {
        luggageStorageService.updateStorageStatus(lsno, status);
        return ResponseEntity.ok().build();
    }
}