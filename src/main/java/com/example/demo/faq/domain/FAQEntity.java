package com.example.demo.faq.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "faqs")
public class FAQEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fno;

    private String fquestion;

    private String fanswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cqno")
    private CategoryEntity category;

}