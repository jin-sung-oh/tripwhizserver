package com.example.demo.product.domain;

import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.SubCategory;
import com.example.demo.category.domain.ThemeCategory;
import com.example.demo.product.dto.ProductListDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pno;

    @Column(nullable = false, length = 50)
    private String pname;

    @Lob
    private String pdesc;

    private int price;

    private boolean delFlag;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    private List<Image> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cno")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scno")
    private SubCategory subCategory;

    @Enumerated(EnumType.STRING)
    private ThemeCategory themeCategory;

    public void changeDelFlag(boolean newDelFlag) {
        this.delFlag = newDelFlag;
    }

    public void addImage(String filename) {
        images.add(new Image(images.size(), filename));
    }

    public void clearImages() {
        images.clear();
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSubCategory(SubCategory subCategory) {
        this.subCategory = subCategory;
    }

    public void setThemeCategory(ThemeCategory themeCategory) {
        this.themeCategory = themeCategory;
    }

    public void updateFromDTO(ProductListDTO productListDTO) {
        this.pname = productListDTO.getPname();
        this.price = productListDTO.getPrice();
        this.themeCategory = productListDTO.getThemeCategory();
        this.images = productListDTO.getImages();
    }
}
