package com.example.demo.product.repository.search;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;
import org.springframework.data.domain.Page;

import java.util.Optional;


public interface ProductSearch {

    PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO);

//    PageResponseDTO<ProductListDTO> listByCno(PageRequestDTO pageRequestDTO);

    // 상위 카테고리 cno로 상품 목록 조회
    PageResponseDTO<ProductListDTO> findByCategory(Long cno, PageRequestDTO pageRequestDTO);

    // cno와 하위 카테고리 scno로 상품 목록 조회
    PageResponseDTO<ProductListDTO> findByCategoryAndSubCategory(Long cno, Long scno, PageRequestDTO pageRequestDTO);

    // 테마 카테고리 tno로 상품 목록 조회
    PageResponseDTO<ProductListDTO> findByThemeCategory(Optional<Long> tno, PageRequestDTO pageRequestDTO);
}

