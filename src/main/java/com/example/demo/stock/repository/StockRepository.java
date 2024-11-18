package com.example.demo.stock.repository;

import com.example.demo.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    // 상품 ID로 재고 조회
    @Query("select s from Stock s where s.product.pno = :productId")
    Optional<Stock> findByProductId(@Param("productId") Long productId);

}
