package com.example.demo.faq.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.dto.FAQListDTO;
import com.example.demo.faq.service.FAQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FAQController {

    private final FAQService faqService;

    // 리스트 조회
    @GetMapping("/list")
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        return faqService.list(pageRequestDTO);

    }

    // 추가
    @PostMapping("/add")
    public ResponseEntity<Long> addFaq(@RequestBody FAQEntity faq) {

        FAQEntity savedFaq = faqService.addFaq(faq);
        Long fno = savedFaq.getFno(); // 저장된 FAQ의 fno 값 추출
        return ResponseEntity.status(HttpStatus.CREATED).body(fno); // fno 값을 반환

    }



}
