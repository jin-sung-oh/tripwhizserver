package com.example.demo.faq.controller;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.faq.dto.FAQListDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
public class FAQController {

    @Autowired
    private final FAQService faqService;

    // 리스트 조회
    @GetMapping("list")
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        return faqService.list(pageRequestDTO);

    }

    // 삭제
    @DeleteMapping("/{fno}")
    public ResponseEntity<Void> softDeleteFAQ(@PathVariable Long fno) {
        faqService.softDeleteFAQ(fno);
        return ResponseEntity.ok().build();
    }


}
