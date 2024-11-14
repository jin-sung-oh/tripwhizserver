package com.example.demo.product.repository.search;

import com.example.demo.category.domain.QCategoryProduct;
import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.product.domain.Product;
import com.example.demo.product.domain.QImage;
import com.example.demo.product.domain.QProduct;
import com.example.demo.product.dto.ProductListDTO;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class ProductSearchImpl extends QuerydslRepositorySupport implements ProductSearch {

    public ProductSearchImpl() {
        super(Product.class);
    }

    @Override
    public Page<Product> list(Pageable pageable) {
        log.info("-------------------list-----------");

        QProduct product = QProduct.product;
        QImage image = QImage.image;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.images, image);

        query.where(image.ord.eq(0));
        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                image.fileUrl
        );

        tupleQuery.fetch();
        return null; // 실제 반환값을 설정하거나 필요한 경우 메서드를 삭제하세요
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByCno(PageRequestDTO pageRequestDTO) {
        return listByCategory(pageRequestDTO.getCategoryCno(), pageRequestDTO);
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByCategory(Long cno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QImage image = QImage.image;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;

        JPQLQuery<Product> query = from(product);
        query.leftJoin(categoryProduct).on(categoryProduct.product.eq(product));
        query.leftJoin(product.images, image);

        query.where(image.ord.eq(0));
        if (cno != null) {
            query.where(product.category.cno.eq(cno));
        }

        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                image.fileUrl
        );

        List<Tuple> tupleList = tupleQuery.fetch();
        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = tupleList.stream().map(t -> {
            Product productObj = t.get(product);
            String fileUrl = t.get(image.fileUrl);

            return ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .price(productObj.getPrice())
                    .fileUrl(fileUrl)
                    .build();
        }).collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<ProductListDTO> listBySubCategory(Long scno, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QImage image = QImage.image;
        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.images, image);

        if (scno != null) {
            query.where(product.subCategory.scno.eq(scno));
        }

        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                image.fileUrl
        );

        List<Tuple> tupleList = tupleQuery.fetch();
        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = tupleList.stream().map(t -> {
            Product productObj = t.get(product);
            String fileUrl = t.get(image.fileUrl);

            return ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .price(productObj.getPrice())
                    .fileUrl(fileUrl)
                    .build();
        }).collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<ProductListDTO> listByTheme(String themeCategory, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,
                pageRequestDTO.getSize(),
                Sort.by("pno").descending()
        );

        QProduct product = QProduct.product;
        QImage image = QImage.image;
        JPQLQuery<Product> query = from(product);
        query.leftJoin(product.images, image);

        if (themeCategory != null) {
            query.where(product.themeCategory.stringValue().eq(themeCategory));
        }

        query.groupBy(product);

        this.getQuerydsl().applyPagination(pageable, query);

        JPQLQuery<Tuple> tupleQuery = query.select(
                product,
                image.fileUrl
        );

        List<Tuple> tupleList = tupleQuery.fetch();
        if (tupleList.isEmpty()) {
            return null;
        }

        List<ProductListDTO> dtoList = tupleList.stream().map(t -> {
            Product productObj = t.get(product);
            String fileUrl = t.get(image.fileUrl);

            return ProductListDTO.builder()
                    .pno(productObj.getPno())
                    .pname(productObj.getPname())
                    .price(productObj.getPrice())
                    .fileUrl(fileUrl)
                    .build();
        }).collect(Collectors.toList());

        long total = query.fetchCount();

        return PageResponseDTO.<ProductListDTO>withAll()
                .dtoList(dtoList)
                .totalCount(total)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }
}
