package com.example.demo.category.dto;


import com.example.demo.category.domain.Category;
import com.example.demo.category.domain.ParentCategory;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long cno;

    private ParentCategory parentCategory;

//    public static CategoryDTO fromEntity(Category category) {
//        CategoryDTO dto = new CategoryDTO();
//        dto.setCno(category.getCno());
//        dto.setCategory(category.getCategory());
//        return dto;
//    }

}
