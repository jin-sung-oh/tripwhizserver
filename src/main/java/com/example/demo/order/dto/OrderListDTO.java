package com.example.demo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {

    private Long ono; // 주문 번호
    private Long mno; // Member ID
    private String name; // Member 이름
    private int totalAmount; // 총 금액
    private int totalPrice; // 총 가격
    private LocalDateTime createTime; // 생성 시간
    private LocalDateTime pickUpDate; // 픽업 시간
    private String status; // 주문 상태
    private boolean delFlag; // 삭제 여부

}

