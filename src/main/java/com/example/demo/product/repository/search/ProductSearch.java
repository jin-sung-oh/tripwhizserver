package com.example.demo.product.repository.search;


import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.dto.ProductListDTO;

public interface ProductSearch {

    //상품 필터링으로 목록 조회
    PageResponseDTO<ProductListDTO> findByFiltering(Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO);
}


