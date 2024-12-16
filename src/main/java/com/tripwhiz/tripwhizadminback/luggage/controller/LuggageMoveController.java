package com.tripwhiz.tripwhizadminback.luggage.controller;

import com.tripwhiz.tripwhizadminback.luggage.dto.LuggageMoveDTO;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageMove;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageMoveStatus;
import com.tripwhiz.tripwhizadminback.luggage.service.LuggageMoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/storeowner/luggagemove")
@RequiredArgsConstructor
public class LuggageMoveController {

    private final LuggageMoveService luggageMoveService;

    // 인증 없이 접근 가능
    @PostMapping("/create")
    public ResponseEntity<LuggageMoveDTO> createLuggageMove(@RequestBody LuggageMove luggageMove) {
        LuggageMoveDTO luggageMoveDTO = luggageMoveService.createLuggageMove(luggageMove);
        return ResponseEntity.ok(luggageMoveDTO);
    }

    // 점주 로그인 필요
    @GetMapping("/list/{lmno}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<LuggageMoveDTO> getLuggageMoveDetails(@PathVariable Long lmno) {
        LuggageMoveDTO luggageMoveDTO = luggageMoveService.getLuggageMoveDetails(lmno);
        return ResponseEntity.ok(luggageMoveDTO);
    }

    // 점주 로그인 필요
    @GetMapping("/list")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<List<LuggageMoveDTO>> getAllLuggageMoves() {
        List<LuggageMoveDTO> luggageMoves = luggageMoveService.getAllLuggageMoves();
        return ResponseEntity.ok(luggageMoves);
    }

    // 점주 로그인 필요
    @PutMapping("/{lmno}/status")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<Void> updateMoveStatus(@PathVariable Long lmno, @RequestParam LuggageMoveStatus status) {
        luggageMoveService.updateMoveStatus(lmno, status);
        return ResponseEntity.ok().build();
    }
}
