package com.tripwhiz.tripwhizadminback.product.repository.search;


import com.tripwhiz.tripwhizadminback.common.dto.PageRequestDTO;
import com.tripwhiz.tripwhizadminback.common.dto.PageResponseDTO;
import com.tripwhiz.tripwhizadminback.product.dto.ProductListDTO;

public interface ProductSearch {

    //상품 필터링으로 목록 조회
    PageResponseDTO<ProductListDTO> findByFiltering(Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO);

    PageResponseDTO<ProductListDTO> searchWithKeywordAndFilters(
            String keyword, Integer minPrice, Integer maxPrice,
            Long tno, Long cno, Long scno, PageRequestDTO pageRequestDTO);

}


