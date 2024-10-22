package com.example.demo.qna.dto;

import com.example.demo.qna.domain.QnAStatus;
import com.example.demo.qna.domain.QuestionsEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class QuestionDTO {

    private Long qno;
    private String title;
    private QnAStatus status;
    private String writer;
    private String qcontent;
    private LocalDateTime createdDate;  // BaseEntity에서 상속된 필드
    private int viewCount;

    // QuestionsEntity 객체를 인자로 받는 생성자 추가
    public QuestionDTO(QuestionsEntity questionsEntity) {
        this.qno = questionsEntity.getQno();
        this.title = questionsEntity.getTitle();
        this.status = questionsEntity.getStatus();
        this.writer = questionsEntity.getWriter();
        this.qcontent = questionsEntity.getQcontent();
        this.createdDate = questionsEntity.getCreatedDate();  // BaseEntity에서 상속된 필드 사용
        this.viewCount = questionsEntity.getViewCount();
    }

    // QuestionsEntity로 변환하는 메서드
    public QuestionsEntity toEntity() {
        return QuestionsEntity.builder()
                .qno(this.qno)
                .title(this.title)
                .status(this.status)
                .writer(this.writer)
                .qcontent(this.qcontent)
                .viewCount(this.viewCount)
                .build();
    }
}
