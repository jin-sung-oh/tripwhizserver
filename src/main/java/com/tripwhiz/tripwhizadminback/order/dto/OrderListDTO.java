package com.tripwhiz.tripwhizadminback.order.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderListDTO {

    private Long ono; // 주문 번호
    private String email;
    private Long spno;
    private int totalAmount; // 총 금액
    private int totalPrice; // 총 가격
    private LocalDateTime createTime; // 생성 시간
    private LocalDateTime pickUpDate; // 픽업 시간
    private String status; // 주문 상태


}

