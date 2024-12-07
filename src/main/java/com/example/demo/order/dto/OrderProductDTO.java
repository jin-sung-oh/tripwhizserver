package com.example.demo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO {

    private Long pno;   // 상품 번호
    private int price;  // 상품 가격
    private int amount; // 수량
}
