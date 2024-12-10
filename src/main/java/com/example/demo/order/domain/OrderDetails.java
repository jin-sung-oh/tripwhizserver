package com.example.demo.order.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long odno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ono", nullable = false)
    private Order order; // Order와 다대일 관계

    @Column(nullable = false)
    private Long pno;     // 상품 번호

    @Column(nullable = false)
    private String pname;  // 상품명

    @Column(nullable = false)
    private int price;           // 상품 가격

    @Column(nullable = false)
    private int amount;          // 상품 수량

    @Column(nullable = true)
    private String qrCodePath; // QR 코드 파일명 (주문 항목별로 저장)
}
