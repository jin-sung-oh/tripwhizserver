//package com.example.demo.category.service;
//
//
//import com.example.demo.category.dto.SubCategoryDTO;
//import com.example.demo.category.repository.SubCategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Transactional
//public class SubCategoryService {
//
//    private final SubCategoryRepository subCategoryRepository;
//
//    @Autowired
//    public SubCategoryService(SubCategoryRepository subCategoryRepository) {
//        this.subCategoryRepository = subCategoryRepository;
//    }
//    public List<SubCategoryDTO> getSubCategoriesByCategory(Long cno) {
//        return subCategoryRepository.findByCategory_Cno(cno).stream()
//                .map(SubCategoryDTO::fromEntity)
//                .collect(Collectors.toList());
//    }
//
//}
