package com.example.demo.product.dto;


import com.example.demo.category.domain.ThemeCategory;
import com.example.demo.product.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductReadDTO {

    private Long pno;               // 상품 번호
    private String pname;            // 상품 이름
    private String pdesc;            // 상품 설명
    private int price;               // 상품 가격
    private Long categoryCno;        // 상위 카테고리 ID
    private Long subCategoryScno;    // 하위 카테고리 ID
    private ThemeCategory themeCategory; // 테마 카테고리

    // Product 엔티티를 ProductReadDTO로 변환하는 정적 메서드
    public static ProductReadDTO fromEntity(Product product) {
        return ProductReadDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .pdesc(product.getPdesc())
                .price(product.getPrice())
                .categoryCno(product.getCategory() != null ? product.getCategory().getCno() : null)
                .subCategoryScno(product.getSubCategory() != null ? product.getSubCategory().getScno() : null)
                .themeCategory(product.getThemeCategory())
                .build();
    }

}