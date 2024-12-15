package com.tripwhiz.tripwhizadminback.stock.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pono;

    private int totalamount;

    private int totalprice;

    @CreatedDate // 생성 시간 자동 기록
    private LocalDateTime createdDate = LocalDateTime.now();

    @Builder.Default
    private PurchaseOrderStatus status = PurchaseOrderStatus.승인대기;

}
