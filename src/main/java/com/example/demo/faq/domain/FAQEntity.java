package com.example.demo.faq.domain;

import com.example.demo.common.domain.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "faqs")
@Builder
public class FAQEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    @Column(nullable = false)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    private int viewCnt = 0;

    @Column(nullable = false)
    private boolean delFlag = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", nullable = false)
    private CategoryEntity category;

    public void updateFields(CategoryEntity category, String question, String answer) {
        this.category = category;
        this.question = question;
        this.answer = answer;
    }



}