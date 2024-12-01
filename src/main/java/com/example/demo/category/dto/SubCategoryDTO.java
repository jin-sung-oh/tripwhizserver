package com.example.demo.category.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubCategoryDTO {

    private Long scno;

    @NotNull
    private String sname;

    private Long cno;


}
