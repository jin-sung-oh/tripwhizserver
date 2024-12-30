package com.tripwhiz.tripwhizadminback.qna.repository.search;

import com.tripwhiz.tripwhizadminback.qna.domain.QnAStatus;
import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionSearch {

    Page<Questions> list(String keyword, QnAStatus status, Pageable pageable);

}
