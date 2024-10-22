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
    private String img1;

    @Column(nullable = true)
    private String img2;

    private boolean delFlag;

    @Column(nullable = false)
    private boolean isPublic = true;

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;


    public void changeImg1(String img1) {
        this.img1 = img1;
    }
    public void changeImg2(String img2) {
        this.img2 = img2;
    }

    public void changeStatus(QnAStatus newStatus) {
        this.status = newStatus;
    }

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }


}
