package com.example.demo.order.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReadDTO {

    private Long ono;
    private int totalPrice;
    private String status;
    private String qrCodePath; // QR 코드 파일명
}