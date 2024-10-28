package com.example.demo.faq.domain;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FaqCategory category = FaqCategory.APP;

    public void updateFields(FaqCategory updatedCategory, String question, String answer) {
        this.category = updatedCategory;
        this.question = question;
        this.answer = answer;
    }
}
