package com.tripwhiz.tripwhizadminback.qna.repository;

import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Questions,Long> {

    //공개된 질문들만 보여줌
    List<Questions> findByIsPublicTrue();

    Page<Questions> findByTitleContaining(String keyword, Pageable pageable);

    Page<Questions> findByQcontentContaining(String keyword, Pageable pageable);

}
