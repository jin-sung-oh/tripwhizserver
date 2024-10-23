package com.example.demo.faq.controller;

import com.example.demo.common.domain.CategoryEntity;
import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.common.repository.CategoryRepository;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.dto.FAQListDTO;
import com.example.demo.faq.dto.FAQModifyDTO;
import com.example.demo.faq.service.FAQService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/faqs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FAQController {

    private final FAQService faqService;

    private final CategoryRepository categoryRepository;

    // 리스트 조회
    @GetMapping("/list")
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        return faqService.list(pageRequestDTO);

    }

    // 추가
    @PostMapping("/")
    public ResponseEntity<Long> addFaq(@RequestBody FAQEntity faq) {

        FAQEntity savedFaq = faqService.addFaq(faq);
        Long fno = savedFaq.getFno(); // 저장된 FAQ의 fno 값 추출
        return ResponseEntity.status(HttpStatus.CREATED).body(fno); // fno 값을 반환

    }

    // 수정
    @PutMapping("/{fno}")
    public ResponseEntity<Void> modifyFaq(
            @PathVariable("fno") Long fno,
            @RequestBody FAQModifyDTO modifyDTO) {


        log.info("-----------------------------------");
        log.info("fno: " + fno);
        log.info("modifyDTO: " + modifyDTO);

        // CategoryEntity를 조회하고, FAQ 수정
        CategoryEntity category = categoryRepository.findById(modifyDTO.getCategory().getCno())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 존재하지 않습니다. categoryId: " + modifyDTO.getCategory().getCno()));

        faqService.modify(fno, category, modifyDTO.getQuestion(), modifyDTO.getAnswer());
        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/{fno}")
    public ResponseEntity<Void> softDeleteFAQ(@PathVariable Long fno) {

        // fno가 존재하지 않으면 에러 반환
        if (!faqService.existsById(fno)) {
            return ResponseEntity.notFound().build();
        }

        faqService.softDeleteFAQ(fno);
        return ResponseEntity.ok().build();

    }
}
