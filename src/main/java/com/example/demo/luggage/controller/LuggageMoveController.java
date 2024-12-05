package com.example.demo.luggage.controller;

import com.example.demo.luggage.dto.LuggageMoveDTO;
import com.example.demo.luggage.entity.LuggageMove;
import com.example.demo.luggage.entity.LuggageMoveStatus;
import com.example.demo.luggage.service.LuggageMoveService;
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

    // 이동 신청 생성
    @PostMapping("/create")
    @PreAuthorize("hasRole('ANONYMOUS')")
    public ResponseEntity<LuggageMoveDTO> createLuggageMove(@RequestBody LuggageMove luggageMove) {
        LuggageMoveDTO luggageMoveDTO = luggageMoveService.createLuggageMove(luggageMove);
        return ResponseEntity.ok(luggageMoveDTO);
    }

    // 이동 상세 정보 조회
    @GetMapping("/list/{lmno}")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<LuggageMoveDTO> getLuggageMoveDetails(@PathVariable Long lmno) {
        LuggageMoveDTO luggageMoveDTO = luggageMoveService.getLuggageMoveDetails(lmno);
        return ResponseEntity.ok(luggageMoveDTO);
    }

    // 모든 이동 리스트 조회
    @GetMapping("/list")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<List<LuggageMoveDTO>> getAllLuggageMoves() {
        List<LuggageMoveDTO> luggageMoves = luggageMoveService.getAllLuggageMoves();
        return ResponseEntity.ok(luggageMoves);
    }

    // 이동 상태 업데이트
    @PutMapping("/{lmno}/status")
    @PreAuthorize("hasRole('STOREOWNER')")
    public ResponseEntity<Void> updateMoveStatus(@PathVariable Long lmno, @RequestParam LuggageMoveStatus status) {
        luggageMoveService.updateMoveStatus(lmno, status);
        return ResponseEntity.ok().build();
    }
}
