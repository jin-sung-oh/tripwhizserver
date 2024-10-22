package com.example.demo.qna.repository.search;

import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.domain.QuestionsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionSearch {

    Page<QuestionsEntity> list(String keyword, QnAStatus status, Pageable pageable);

}
