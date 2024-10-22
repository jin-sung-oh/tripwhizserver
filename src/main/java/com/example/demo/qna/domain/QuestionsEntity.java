package com.example.demo.qna.domain;


import jakarta.persistence.*;
import lombok.*;

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
    private String question;

    @Column(nullable = false, length = 100)
    private String title;

    private String writer;

    @Builder.Default
    private QnAStatus status = QnAStatus.답변대기;

    @Column(nullable = true)
    private String images;


    private boolean delFlag;

    @Column(nullable = false)
    private boolean isPublic = true;

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;


    public void changeImages(String newImages) {
        this.images = newImages;
    }


    public void changeStatus(QnAStatus newStatus) {
        this.status = newStatus;
    }

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }


}
