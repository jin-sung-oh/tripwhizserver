//package com.example.demo.product.repository.search;
//
//
//import com.example.demo.common.dto.PageRequestDTO;
//import com.example.demo.common.dto.PageResponseDTO;
//import com.example.demo.product.domain.Product;
//import com.example.demo.product.dto.ProductListDTO;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//
//
//public interface ProductSearch {
//
//    Page<Product> list(Pageable pageable);
//
//    PageResponseDTO<ProductListDTO> listByCno(PageRequestDTO pageRequestDTO);
//
//    // 상위 카테고리 cno로 상품 목록 조회
//    PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO);
//
//    // 하위 카테고리 scno로 상품 목록 조회
//    PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO pageRequestDTO);
//
//    // 테마 카테고리(ThemeCategory enum 값)로 상품 목록 조회
//    PageResponseDTO<ProductListDTO> listByTheme(String themeCategory, PageRequestDTO pageRequestDTO);
//}
//
//
