package com.example.demo.category.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "category")
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scno;

    @Column(nullable = false)
    private String sname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cno")
    private Category category;

}
