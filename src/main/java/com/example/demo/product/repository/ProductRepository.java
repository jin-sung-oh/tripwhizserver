package com.example.demo.product.repository;


import com.example.demo.product.domain.Product;
import com.example.demo.product.dto.ProductReadDTO;
import com.example.demo.product.repository.search.ProductSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductSearch {

    // 특정 상품을 ProductReadDTO 형태로 조회
    @Query("select new com.example.demo.product.dto.ProductReadDTO(" +
            "p.pno, p.pname, p.pdesc, p.price, " +
            "p.category.cno, p.subCategory.scno) " +
            "from Product p " +
            "where p.pno = :pno")
    Optional<ProductReadDTO> read(@Param("pno") Long pno);

 }
