package com.example.demo.order.domain;

import com.example.demo.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ono", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int amount;
}