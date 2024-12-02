package com.example.demo.luggage.controller;

import com.example.demo.luggage.dto.LuggageDTO;
import com.example.demo.luggage.service.LuggageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/luggage")
public class LuggageController {

    @Autowired
    private LuggageService luggageService;

    // 수화물 보관/이동 요청 저장
    @PostMapping("/save")
    public String saveLuggage(@RequestBody LuggageDTO luggageDTO) {
        luggageService.saveLuggage(luggageDTO);
        return "Luggage saved successfully for user: " + luggageDTO.getEmail();
    }

    // 수화물 보관 승인
    @PostMapping("/approve/storage/{id}")
    public String approveStorageLuggage(@PathVariable Long id) {
        luggageService.approveLuggage(id, true); // true = 보관
        return "Luggage storage request approved successfully for id: " + id;
    }

    // 수화물 이동 승인
    @PostMapping("/approve/moving/{id}")
    public String approveMovingLuggage(@PathVariable Long id) {
        luggageService.approveLuggage(id, false); // false = 이동
        return "Luggage moving request approved successfully for id: " + id;
    }

    // 수화물 요청 거절
    @PostMapping("/reject/{id}")
    public String rejectLuggage(@PathVariable Long id) {
        luggageService.rejectLuggage(id);
        return "Luggage request rejected successfully for id: " + id;
    }
}
