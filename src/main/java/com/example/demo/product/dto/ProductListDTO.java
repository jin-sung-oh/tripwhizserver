//package com.example.demo.product.dto;
//
//import com.example.demo.category.domain.Category;
//import com.example.demo.category.domain.SubCategory;
//import com.example.demo.category.domain.ThemeCategory;
//import com.example.demo.product.domain.Image;
//import com.example.demo.product.domain.Product;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.List;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class ProductListDTO {
//
//    private Long pno;
//    private String pname;
//    private int price;
//    private Long categoryCno;
//    private Long subCategoryScno;
//    private ThemeCategory themeCategory;
//    private List<Image> images;
//
//    public Product toEntity(Category category, SubCategory subCategory) {
//        return Product.builder()
//                .pname(this.pname)
//                .price(this.price)
//                .category(category)
//                .subCategory(subCategory)
//                .themeCategory(this.themeCategory)
//                .images(this.images)
//                .build();
//    }
//}
