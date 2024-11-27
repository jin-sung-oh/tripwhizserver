package com.example.demo.product.repository.search;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.QProduct;
import com.example.demo.product.domain.QProductTheme;
import com.example.demo.product.domain.QThemeCategory;
import com.example.demo.product.dto.ProductListDTO;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public PageResponseDTO<ProductListDTO> list(PageRequestDTO pageRequestDTO) {
        log.info("-------------------list-----------");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        JPQLQuery<Product> query = from(product);

        query.leftJoin(product.attachFiles).fetchJoin();
        query.groupBy(product);

        // 페이징 적용
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();

    }

//    @Override
//    public PageResponseDTO<ProductListDTO> listByCno(PageRequestDTO pageRequestDTO) {
//        return listByCategory(pageRequestDTO.getCategoryCno(), pageRequestDTO);
//    }

    @Override
    public PageResponseDTO<ProductListDTO> findByCategory(Long cno, PageRequestDTO pageRequestDTO) {

        log.info("Fetching product list by category ID: {}", cno);

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles).fetchJoin()
                .where(product.category.cno.eq(cno))
                .groupBy(product);

        // 페이징 적용
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();

//        this.getQuerydsl().applyPagination(pageable, query);
//
//        JPQLQuery<Product> tupleQuery = query.select(product);
//
//        List<Product> productList = tupleQuery.fetch();
//        if (productList.isEmpty()) {
//            return null;
//        }
//
//        List<ProductListDTO> dtoList = productList.stream().map(productObj ->
//            ProductListDTO.builder()
//                    .pno(productObj.getPno())
//                    .pname(productObj.getPname())
//                    .price(productObj.getPrice())
//                    .attachFiles(productObj.getAttachFiles()) // JH
//                    .build()
//        ).collect(Collectors.toList());
//
//        long total = query.fetchCount();
//
//        return PageResponseDTO.<ProductListDTO>withAll()
//                .dtoList(dtoList)
//                .totalCount(total)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
    }

    @Override
    public PageResponseDTO<ProductListDTO> findByCategoryAndSubCategory(Long cno, Long scno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;

        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles).fetchJoin()
                .where(product.category.cno.eq(cno).and(product.subCategory.scno.eq(scno)));

        // 페이징 적용
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();

//        if (scno != null) {
//            query.where(product.subCategory.scno.eq(scno));
//        }

//        query.groupBy(product);
//
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        JPQLQuery<Product> tupleQuery = query.select(product);
//
//        List<Product> productList = tupleQuery.fetch();
//        if (productList.isEmpty()) {
//            return null;
//        }
//
//        List<ProductListDTO> dtoList = productList.stream().map(productObj ->
//                ProductListDTO.builder()
//                        .pno(productObj.getPno())
//                        .pname(productObj.getPname())
//                        .price(productObj.getPrice())
//                        .attachFiles(productObj.getAttachFiles()) // JH
//                        .build()
//        ).collect(Collectors.toList());
//
//        long total = query.fetchCount();
//
//        return PageResponseDTO.<ProductListDTO>withAll()
//                .dtoList(dtoList)
//                .totalCount(total)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
    }

    @Override
    public PageResponseDTO<ProductListDTO> findByThemeCategory(Optional<Long> tno, PageRequestDTO pageRequestDTO) {

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QProductTheme productTheme = QProductTheme.productTheme;
        QThemeCategory themeCategory = QThemeCategory.themeCategory;

        JPQLQuery<Product> query = from(product)
                .leftJoin(product.attachFiles).fetchJoin()
                .innerJoin(productTheme).on(product.eq(productTheme.product))
                .innerJoin(themeCategory).on(productTheme.themeCategory.eq(themeCategory));

        //tno가 존재하면 필터 조건 추가
        tno.ifPresent(theme -> query.where(themeCategory.tno.eq(theme)));


        // 페이징 적용 및 데이터 조회
        List<Product> productList = getQuerydsl().applyPagination(pageable, query)
                .select(product)
                .fetch();

        // DTO 변환
        List<ProductListDTO> dtoList = productList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        // 총 개수 조회
        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(total)
                .build();

    }

    private ProductListDTO convertToDto(Product product) {
        return ProductListDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .price(product.getPrice())
                .categoryCno(product.getCategory() != null ? product.getCategory().getCno() : null)
                .subCategoryScno(product.getSubCategory() != null ? product.getSubCategory().getScno() : null)
                .build();
    }

//        if (themeCategory != null) {
//            query.where(product.themeCategory.stringValue().eq(themeCategory));
//        }
//
//        query.groupBy(product);
//
//        this.getQuerydsl().applyPagination(pageable, query);
//
//        JPQLQuery<Product> tupleQuery = query.select(product);
//
//        List<Product> productList = tupleQuery.fetch();
//        if (productList.isEmpty()) {
//            return null;
//        }
//
//        List<ProductListDTO> dtoList = productList.stream().map(productObj ->
//                ProductListDTO.builder()
//                        .pno(productObj.getPno())
//                        .pname(productObj.getPname())
//                        .price(productObj.getPrice())
//                        .attachFiles(productObj.getAttachFiles()) // JH
//                        .build()
//        ).collect(Collectors.toList());
//
//        long total = query.fetchCount();
//
//        return PageResponseDTO.<ProductListDTO>withAll()
//                .dtoList(dtoList)
//                .totalCount(total)
//                .pageRequestDTO(pageRequestDTO)
//                .build();
//    }


}