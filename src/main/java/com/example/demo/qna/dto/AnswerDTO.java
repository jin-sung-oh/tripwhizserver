package com.example.demo.qna.dto;

import com.example.demo.qna.domain.QnAStatus;  // QnAStatus로 수정
import com.example.demo.qna.domain.AnswersEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerDTO {
    private Long ano;
    private String acontent;
    private QnAStatus status;
    private String writer;  // writer 필드 추가// QnAStatus로 수정

    // DTO -> Entity 변환
    public AnswersEntity toEntity() {
        AnswersEntity entity = new AnswersEntity();
        entity.setAcontent(this.acontent);  // acontent 설정
        entity.setStatus(this.status);  // QnAStatus 설정
        return entity;
    }
}
