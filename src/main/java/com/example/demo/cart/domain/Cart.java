package com.example.demo.cart.domain;

import com.example.demo.member.domain.Member;
import com.example.demo.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"product"})
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mno", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private int totalQty;

    @Column(nullable = false)
    private int totalPrice;

}
