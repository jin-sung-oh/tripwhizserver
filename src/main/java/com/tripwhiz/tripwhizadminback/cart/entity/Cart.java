package com.tripwhiz.tripwhizadminback.cart.entity;

import com.tripwhiz.tripwhizadminback.member.entity.Member;
import com.tripwhiz.tripwhizadminback.product.entity.Product;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"product", "member"})
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pno", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false)
    @Builder.Default
    private boolean delFlag = false;

    public void setQty(int qty) {
        if (qty < 0) {
            throw new IllegalArgumentException("Quantity cannot be less than zero.");
        }
        this.qty = qty;
    }

    public void softDelete() {
        this.delFlag = true;
    }

}
