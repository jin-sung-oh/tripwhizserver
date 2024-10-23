package com.example.demo.faq.repository;

import com.example.demo.faq.domain.FAQEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FAQRepository extends JpaRepository<FAQEntity, Long>, FAQFilter {

    Page<FAQEntity> findByFno(Long fno, Pageable pageable);

    // 삭제
    @Modifying
    @Query("UPDATE FAQEntity f set f.delFlag = true where f.fno = :fno")
    int softDeleteByFno(@Param("fno") Long fno);

}
