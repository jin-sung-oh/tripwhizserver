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
}
