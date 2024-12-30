package com.tripwhiz.tripwhizadminback.qna.dto;

import com.tripwhiz.tripwhizadminback.qna.domain.QnAStatus;
import com.tripwhiz.tripwhizadminback.qna.domain.QnaCategory;
import com.tripwhiz.tripwhizadminback.qna.domain.Questions;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionDTO {

    private Long qno;
    private String title;
    private QnAStatus status;
    private QnaCategory category;
    private String writer;
    private String qcontent;
    private LocalDateTime createdDate;
    private int viewCount;

    // QuestionsEntity 객체를 인자로 받는 생성자
    public QuestionDTO(Questions questionsEntity) {
        this.qno = questionsEntity.getQno();
        this.title = questionsEntity.getTitle();
        this.status = questionsEntity.getStatus();
        this.category = questionsEntity.getCategory();
        this.writer = questionsEntity.getWriter();
        this.qcontent = questionsEntity.getQcontent();
        this.createdDate = questionsEntity.getCreatedDate();
        this.viewCount = questionsEntity.getViewCount();
    }

    // QuestionsEntity로 변환하는 메서드
    public Questions toEntity() {
        return Questions.builder()
                .qno(this.qno)
                .title(this.title)
                .status(this.status)
                .category(this.category)
                .writer(this.writer)
                .qcontent(this.qcontent)
                .viewCount(this.viewCount)
                .build();
    }
}
