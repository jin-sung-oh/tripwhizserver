package com.example.demo.qna.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswersEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ano;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qno",nullable = false)
    private QuestionsEntity questions;

    @Lob
    private String answers;

    @Column(nullable = false)
    private String writer;

    @Column(nullable = false)
    @Builder.Default
    private int viewCount = 0;

    @Column(nullable = false)
    private boolean isPublic = true;

}
