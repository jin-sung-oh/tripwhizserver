package com.example.demo.qna.repository;

import com.example.demo.qna.domain.QuestionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface QuestionRepository extends JpaRepository<QuestionsEntity,Long> {

    //공개된 질문들만 보여줌
    List<QuestionsEntity> findByIsPublicTrue();

    Page<QuestionsEntity> findByTitleContaining(String keyword, Pageable pageable);

    Page<QuestionsEntity> findByQcontentContaining(String keyword, Pageable pageable);

}
