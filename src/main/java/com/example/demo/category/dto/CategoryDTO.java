package com.example.demo.category.dto;


import com.example.demo.category.domain.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoryDTO {

    private Long cno;

    @NotNull
    private String cname;

    public static CategoryDTO fromEntity(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setCno(category.getCno());
        dto.setCname(category.getCname());
        return dto;
    }

}
