package com.example.demo.qna.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
@Table(name = "questions")
public class QuestionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long qno;

    @Lob
    private String qcontent;

    @Column(nullable = false, length = 100)
    private String title;

    private String writer;

    @Builder.Default
    private QnAStatus status = QnAStatus.답변대기;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)  // 이 필드는 null 값이 아니어야 함
//    private QnaCategory category;
    @Builder.Default
    private QnaCategory category = QnaCategory.APP;

    @ElementCollection // 컬렉션을 매핑하기 위해 사용
    @CollectionTable(name = "question_images", joinColumns = @JoinColumn(name = "question_qno"))
    @Column(name = "images", nullable = true)
    private List<String> images = new ArrayList<>();


    private boolean delFlag;

    @Column(nullable = false)
    private boolean isPublic = true;

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @CreatedDate // 생성 시간 자동 기록
    private LocalDateTime createdDate;

    @LastModifiedDate // 수정 시간 자동 기록
    private LocalDateTime updatedDate;


    public void changeImages(List<String> newImages) {
        this.images = newImages;
    }


    public void changeStatus(QnAStatus newStatus) {
        this.status = newStatus;
    }

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }


}
