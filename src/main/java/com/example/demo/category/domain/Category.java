package com.example.demo.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    // 상위 카테고리 설정 (ParentCategory Enum 사용)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ParentCategory parentCategory;

    private boolean delFlag;

}
