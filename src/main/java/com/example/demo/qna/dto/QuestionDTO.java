package com.example.demo.qna.dto;

import com.example.demo.qna.domain.QnAStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
//QuestionDTO
public class QuestionDTO {

    private Long qno;
    private String title;
    private QnAStatus status;
    private String writer;
    private String qcontent;
    private LocalDateTime createdDate;
    private int viewCount;

    public QuestionDTO(Long qno, String title, QnAStatus status, String writer, String question, LocalDateTime createdDate, int viewCount) {
        this.qno = qno;
        this.title = title;
        this.status = status;
        this.writer = writer;
        this.qcontent = qcontent;
        this.createdDate = createdDate;
        this.viewCount = viewCount;
    }
}


