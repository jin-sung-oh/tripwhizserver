package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
import com.example.demo.faq.domain.FaqCategory;
import com.example.demo.faq.dto.FAQReadDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FAQRepository extends JpaRepository<FAQEntity, Long>, FAQFilter {

    // delFlag가 false인 것만 조회
    @Query("SELECT f FROM FAQEntity f WHERE f.delFlag = false")
    Page<FAQEntity> filteredList(Pageable pageable);

    // 카테고리별로 FAQ 목록을 가져오는 메서드
    @Query("SELECT f FROM FAQEntity f WHERE f.delFlag = false AND (:category IS NULL OR f.category = :category)")
    Page<FAQEntity> filteredList(Pageable pageable, @Param("category") FaqCategory category);

    // 카테고리별 FAQ의 개수를 가져오는 메서드
    @Query("SELECT COUNT(f) FROM FAQEntity f WHERE f.delFlag = false AND (:category IS NULL OR f.category = :category)")
    long countByCategory(@Param("category") FaqCategory category);

    // 아이디가 존재하는지 확인하는 로직
    boolean existsById(Long fno);

    // 조회
    @Query("SELECT new com.example.demo.faq.dto.FAQReadDTO(f.fno, f.category, f.question, f.answer) FROM FAQEntity f WHERE f.fno = :fno")
    Optional<FAQReadDTO> read(@Param("fno") Long fno);

    // 삭제
    @Modifying
    @Query("UPDATE FAQEntity f SET f.delFlag = true WHERE f.fno = :fno")
    int softDeleteByFno(@Param("fno") Long fno);

    // 수정
    @Modifying
    @Query("UPDATE FAQEntity f SET f.question = :question, f.answer = :answer WHERE f.fno = :fno")
    int updateFaq(@Param("fno") Long fno, @Param("question") String question, @Param("answer") String answer);
}
