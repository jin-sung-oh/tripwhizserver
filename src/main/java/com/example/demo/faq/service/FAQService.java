package com.example.demo.faq.service;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.dto.FAQListDTO;
import com.example.demo.faq.repository.FAQRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class FAQService {

    @Autowired
    private final FAQRepository faqRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    // list
    @Transactional
    public PageResponseDTO<FAQListDTO> list(PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("fno").descending());

        Page<FAQEntity> result = faqRepository.findAll(pageable);

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

    // modify
    @Transactional
    public boolean modify(Long fno, FAQModifyDTO modifyDTO) {
        FAQEntity faqEntity = faqRepository.findById(fno)
                .orElseThrow(() -> new IllegalArgumentException("해당 FAQ가 존재하지 않습니다. fno: " + fno));


        FAQEntity updatedFaqEntity = FAQEntity.builder()
                .fno(faqEntity.getFno())
                .category(modifyDTO.getCategory())
                .question(modifyDTO.getQuestion())
                .answer(modifyDTO.getAnswer())
                .viewCnt(faqEntity.getViewCnt())
                .build();

        faqRepository.save(updatedFaqEntity);

        log.info("FAQ 수정 완료: fno={}, category={}, question={}, answer={}",
                updatedFaqEntity.getFno(), modifyDTO.getCategory(), modifyDTO.getQuestion(), modifyDTO.getAnswer());
    // delete
    @Transactional
    public void softDeleteFAQ(Long fno) {

        int updatedRows = faqRepository.softDeleteByFno(fno);

        return true;
    }
        // fno가 0일때 삭제 안되게 처리
        if (updatedRows == 0) {
            throw new IllegalArgumentException("FAQ not found with fno: " + fno);
        }

    }

}
