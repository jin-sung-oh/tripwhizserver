package com.tripwhiz.tripwhizadminback.faq.controller;

import com.tripwhiz.tripwhizadminback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizadminback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizadminback.faq.dto.FaqListDTO;
import com.tripwhiz.tripwhizadminback.faq.dto.FaqModifyDTO;
import com.tripwhiz.tripwhizadminback.faq.dto.FaqReadDTO;
import com.tripwhiz.tripwhizadminback.faq.entity.Faq;
import com.tripwhiz.tripwhizadminback.faq.entity.FaqCategory;
import com.tripwhiz.tripwhizadminback.faq.service.FaqService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api/storeowner/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    // 리스트 조회
    @GetMapping("/list")
    public PageResponseDTO<FaqListDTO> list(PageRequestDTO pageRequestDTO,
                                            @RequestParam(required = false) FaqCategory category) {
        // 카테고리가 null인 경우 모든 FAQ를 조회
        log.info("Fetching FAQ list for category: {}", category);
        return faqService.list(pageRequestDTO, category); // 카테고리 정보를 함께 전달
    }

    // 조회
    @GetMapping("/read/{fno}")
    public ResponseEntity<FaqReadDTO> read(@PathVariable Long fno) {
        log.info(fno);
        FaqReadDTO faqReadDTO = faqService.read(fno);
        log.info(faqReadDTO);

        if (faqReadDTO == null) {
            return ResponseEntity.notFound().build(); // FAQ가 없으면 404 Not Found 반환
        }

        return ResponseEntity.ok(faqReadDTO);
    }

    // 추가
    @PostMapping("/add")
    public ResponseEntity<Long> addFaq(@RequestBody Faq faq) {
        Faq savedFaq = faqService.addFaq(faq);
        Long fno = savedFaq.getFno(); // 저장된 FAQ의 fno 값 추출
        return ResponseEntity.status(HttpStatus.CREATED).body(fno); // fno 값을 반환
    }

    // 수정
    @PutMapping("/update/{fno}")
    public ResponseEntity<Void> modifyFaq(
            @PathVariable("fno") Long fno,
            @RequestBody FaqModifyDTO modifyDTO) {

        log.info("-----------------------------------");
        log.info("fno: " + fno);
        log.info("modifyDTO: " + modifyDTO);

        // FAQ 수정 시 카테고리 정보를 FaqCategory enum으로 처리
        FaqCategory category = modifyDTO.getCategory();

        faqService.modify(fno, category, modifyDTO.getQuestion(), modifyDTO.getAnswer());
        return ResponseEntity.ok().build();
    }

    // 삭제
    @DeleteMapping("/delete/{fno}")
    public ResponseEntity<String> softDeleteFAQ(@PathVariable Long fno) {
        // fno가 존재하지 않으면 에러 반환
        if (!faqService.existsById(fno)) {
            return ResponseEntity.notFound().build();
        }

        faqService.softDeleteFAQ(fno);
        return ResponseEntity.ok("success");
    }
}
