package com.tripwhiz.tripwhizadminback.faq.service;

import com.tripwhiz.tripwhizadminback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizadminback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizadminback.faq.dto.FaqListDTO;
import com.tripwhiz.tripwhizadminback.faq.dto.FaqReadDTO;
import com.tripwhiz.tripwhizadminback.faq.entity.Faq;
import com.tripwhiz.tripwhizadminback.faq.entity.FaqCategory;
import com.tripwhiz.tripwhizadminback.faq.repository.FaqRepository;
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
public class FaqService {

    private final FaqRepository faqRepository;


    // 카테고리별 FAQ 리스트 조회 (페이지네이션 포함)
    @Transactional
    public PageResponseDTO<FaqListDTO> list(PageRequestDTO pageRequestDTO, FaqCategory category) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("fno").descending());


        // 필터링된 FAQ 리스트를 페이지네이션과 함께 가져옴
        Page<Faq> result = faqRepository.filteredList(pageable, category);

        // DTO 리스트 생성
        List<FaqListDTO> dtoList = result.getContent().stream()
                .map(faqEntity -> FaqListDTO.builder()
                        .fno(faqEntity.getFno())
                        .question(faqEntity.getQuestion())
                        .answer(faqEntity.getAnswer())
                        .viewCnt(faqEntity.getViewCnt())
                        .delFlag(faqEntity.isDelFlag())
                        .category(faqEntity.getCategory())
                        .build())
                .collect(Collectors.toList());

        long totalCount = faqRepository.countByCategory(category); // 카테고리별 총 개수 계산

        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / pageRequestDTO.getSize());

        return PageResponseDTO.<FaqListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(totalCount)
//                .totalPage(totalPages) // 총 페이지 수 설정
//                .prev(pageRequestDTO.getPage() > 1) // 이전 버튼 여부
//                .next(pageRequestDTO.getPage() < totalPages) // 다음 버튼 여부
                .build();
    }

    // FAQ 조회
    @Transactional
    public FaqReadDTO read(Long fno) {
        Optional<FaqReadDTO> result = faqRepository.read(fno);
        return result.orElse(null); // 결과가 없으면 null 반환
    }

    // FAQ 추가
    @Transactional
    public Faq addFaq(Faq faq) {
        return faqRepository.save(faq); // FAQ 저장
    }

    // FAQ 수정
    @Transactional
    public boolean modify(Long fno, FaqCategory category, String question, String answer) {
        int updatedRows = faqRepository.updateFaq(fno, question, answer);

        if (updatedRows == 0) {
            throw new IllegalArgumentException("해당 FAQ가 존재하지 않습니다. fno: " + fno);
        }

        return true;
    }

    // FAQ 삭제
    @Transactional
    public void softDeleteFAQ(Long fno) {
        int updatedRows = faqRepository.softDeleteByFno(fno);

        if (updatedRows == 0) {
            throw new IllegalArgumentException("FAQ not found with fno: " + fno);
        }
    }

    // FAQ 존재 여부 확인
    @Transactional(readOnly = true)
    public boolean existsById(Long fno) {
        return faqRepository.existsById(fno);
    }
}
