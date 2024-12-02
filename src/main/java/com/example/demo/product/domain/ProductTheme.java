package com.example.demo.product.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"product", "themeCategory"})
@Table(name = "product_theme")
public class ProductTheme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ptno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_pno", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_category_tno", nullable = false)
    private ThemeCategory themeCategory;

}
