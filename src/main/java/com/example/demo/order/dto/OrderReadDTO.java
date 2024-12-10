package com.example.demo.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadDTO {
    private Long ono; // 주문 번호
    private String email; // 사용자 이메일
    private List<OrderProductDTO> products; // 주문 상품 목록
    private int totalPrice; // 총 가격
    private String status; // 주문 상태
    private LocalDateTime pickUpDate; // 픽업 날짜
    private Long spno; // 지점번호
    private String qrCodePath;

}
