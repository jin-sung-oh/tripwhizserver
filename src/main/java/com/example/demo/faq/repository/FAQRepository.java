package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
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

    // 아이디가 존재하는지 확인하는 로직
    boolean existsById(Long fno);

    // 조회
    @Query("select " +
            "new com.example.demo.faq.dto.FAQReadDTO(f.fno,f.category,f.question,f.answer) " +
            "from FAQEntity f where f.fno = :fno")
    Optional<FAQReadDTO> read(@Param("fno") Long fno);

    // 삭제
    @Modifying
    @Query("UPDATE FAQEntity f set f.delFlag = true where f.fno = :fno")
    int softDeleteByFno(@Param("fno") Long fno);

    //수정
    @Modifying
    @Query("UPDATE FAQEntity f SET f.question = :question, f.answer = :answer WHERE f.fno = :fno")
    int updateFaq(@Param("fno") Long fno,
                  @Param("question") String question,
                  @Param("answer") String answer);

}
