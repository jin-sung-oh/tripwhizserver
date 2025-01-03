package com.tripwhiz.tripwhizadminback.faq.repository;

import com.tripwhiz.tripwhizadminback.faq.dto.FaqReadDTO;
import com.tripwhiz.tripwhizadminback.faq.entity.Faq;
import com.tripwhiz.tripwhizadminback.faq.entity.FaqCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqFilter {

    // delFlag가 false인 것만 조회
    @Query("SELECT f FROM Faq f WHERE f.delFlag = false")
    Page<Faq> filteredList(Pageable pageable);

    // 카테고리별로 FAQ 목록을 가져오는 메서드
    @Query("SELECT f FROM Faq f WHERE f.delFlag = false AND (:category IS NULL OR f.category = :category)")
    Page<Faq> filteredList(Pageable pageable, @Param("category") FaqCategory category);

    // 카테고리별 FAQ의 개수를 가져오는 메서드
    @Query("SELECT COUNT(f) FROM Faq f WHERE f.delFlag = false AND (:category IS NULL OR f.category = :category)")
    long countByCategory(@Param("category") FaqCategory category);

    // 아이디가 존재하는지 확인하는 로직
    boolean existsById(Long fno);

    // 조회
    @Query("SELECT new com.tripwhiz.tripwhizadminback.faq.dto.FaqReadDTO(f.fno, f.category, f.question, f.answer) FROM Faq f WHERE f.fno = :fno")
    Optional<FaqReadDTO> read(@Param("fno") Long fno);

    // 삭제
    @Modifying
    @Query("UPDATE Faq f SET f.delFlag = true WHERE f.fno = :fno")
    int softDeleteByFno(@Param("fno") Long fno);

    // 수정
    @Modifying
    @Query("UPDATE Faq f SET f.question = :question, f.answer = :answer WHERE f.fno = :fno")
    int updateFaq(@Param("fno") Long fno, @Param("question") String question, @Param("answer") String answer);
}