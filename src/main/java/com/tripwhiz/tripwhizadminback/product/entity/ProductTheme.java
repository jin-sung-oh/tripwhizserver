package com.tripwhiz.tripwhizadminback.product.entity;


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

    // 매개변수 생성자
    public ProductTheme(Product product, ThemeCategory themeCategory) {
        this.product = product;
        this.themeCategory = themeCategory;
    }

}
