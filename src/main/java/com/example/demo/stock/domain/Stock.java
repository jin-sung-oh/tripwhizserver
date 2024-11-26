package com.example.demo.stock.domain;

import com.example.demo.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Table(name = "stock")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private Integer quantity; // 현재 재고 수량

    @Column(nullable = false)
    private LocalDateTime lastUpdated; // 재고 최종 수정 시간


    public void updateStock(int newQuantity) {
        this.quantity = newQuantity;
        this.lastUpdated = LocalDateTime.now();
    }

}