package com.example.demo.qna.dto;

import com.example.demo.qna.domain.QnAStatus;  // QnAStatus로 수정
import com.example.demo.qna.domain.AnswersEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerDTO {

    private Long ano;

    @NotNull(message = "내용은 필수 입력 항목입니다.")
    private String acontent;

    private QnAStatus status;

    @NotNull(message = "작성자는 필수 입력 항목입니다.")
    private String writer;  // writer 필드 추가// QnAStatus로 수정

    private LocalDateTime createdDate;

    public AnswersEntity toEntity() {
        AnswersEntity entity = new AnswersEntity();
        entity.setAno(this.ano);  // ano 설정 (이미 존재하는 답변 수정 시 필요)
        entity.setAcontent(this.acontent);  // acontent 설정 (답변 내용)
        entity.setStatus(this.status);  // QnAStatus 설정 (답변 상태)
        entity.setWriter(this.writer);  // writer 설정 (작성자)
        entity.setCreatedDate(this.createdDate);  // 생성 날짜 설정 (작성일자)

        return entity;
    }
}
