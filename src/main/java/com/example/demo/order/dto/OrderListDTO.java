package com.example.demo.order.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderListDTO {

    private Long ono;
    private String email;
    private Long spno;
    private int totalAmount;
    private int totalPrice;
    private LocalDateTime createTime;
    private LocalDateTime pickUpDate;
    private String status;
    private boolean delFlag;
    private String qrCodePath; // QR 코드 파일명
}
