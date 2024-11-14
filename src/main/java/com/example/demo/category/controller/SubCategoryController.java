package com.example.demo.category.controller;


import com.example.demo.category.dto.SubCategoryDTO;
import com.example.demo.category.service.SubCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/subcategory")
public class SubCategoryController {

    private final SubCategoryService subCategoryService;

    @GetMapping("/category/{cno}")
    public ResponseEntity<List<SubCategoryDTO>> findCategoryByCno(@RequestParam("cno") Long cno) {
        List<SubCategoryDTO> subCategories = subCategoryService.getSubCategoriesByCategory(cno);
        log.info("findCategoryByCno: " + cno);
        return ResponseEntity.ok(subCategories);

    }
}
