package com.example.demo.faq.service;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.domain.FaqCategory;
import com.example.demo.faq.dto.FAQListDTO;
import com.example.demo.faq.dto.FAQReadDTO;
import com.example.demo.faq.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FAQService {

    private final FAQRepository faqRepository;

    // list
    @Transactional
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("fno").descending());

        Page<FAQEntity> result = faqRepository.filteredList(pageable);

        List<FAQListDTO> dtoList = result.get().map(FAQEntity -> {

            FAQListDTO dto = FAQListDTO.builder()
                    .fno(FAQEntity.getFno())
                    .question(FAQEntity.getQuestion())
                    .answer(FAQEntity.getAnswer())
                    .viewCnt(FAQEntity.getViewCnt())
                    .delFlag(false)
                    .build();

            return dto;

        }).collect(Collectors.toUnmodifiableList());

        long total = result.getTotalElements();

        return PageResponseDTO.<FAQListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();

    }

    // read
    @Transactional
    public FAQReadDTO read(Long fno) {

        Optional<FAQReadDTO> result = faqRepository.read(fno);

        if (result.isEmpty()) {

            log.info(result);
            return null;

        }

        return result.get();

    }

    // add
    @Transactional
    public FAQEntity addFaq(FAQEntity faq) {

        // FAQ 저장
        FAQEntity savedFaq = faqRepository.save(faq);
        return savedFaq; // 저장된 FAQ 반환

    }

    // modify
    @Transactional
    public boolean modify(Long fno, FaqCategory category, String question, String answer) {
        // 업데이트 실행
        int updatedRows = faqRepository.updateFaq(fno, question, answer);

        // 업데이트된 행이 없으면 예외 발생
        if (updatedRows == 0) {
            throw new IllegalArgumentException("해당 FAQ가 존재하지 않습니다. fno: " + fno);
        }

        return true;
    }

    // delete
    @Transactional
    public void softDeleteFAQ(Long fno) {

        int updatedRows = faqRepository.softDeleteByFno(fno);

        // fno가 0일때 삭제 안되게 처리
        if (updatedRows == 0) {
            throw new IllegalArgumentException("FAQ not found with fno: " + fno);
        }

    }

    // existsById 추가
    @Transactional(readOnly = true)
    public boolean existsById(Long fno) {
        return faqRepository.existsById(fno);
    }


}
