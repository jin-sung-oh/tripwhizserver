package com.tripwhiz.tripwhizadminback.qna.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qno", nullable = false)
    private Questions questions;

    @Lob
    private String acontent;  // acontent 필드


    @Builder.Default
    private QnAStatus status = QnAStatus.답변대기;  // QnAStatus 필드

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @Column(nullable = false)
    private boolean isPublic = true;

    @CreatedDate // 생성 시간 자동 기록
    private LocalDateTime createdDate;

    @LastModifiedDate // 수정 시간 자동 기록
    private LocalDateTime updatedDate;

    // 상태 변경 메서드
    public void setStatus(QnAStatus status) {
        this.status = status;
    }

    public void setAcontent(String acontent) {
        this.acontent = acontent;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
    }
}
