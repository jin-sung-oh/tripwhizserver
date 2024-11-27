package com.example.demo.stock.dto;


import lombok.*;

import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseOrderDetailDTO {

    private Long podno;

    private int amount;

    private LocalDateTime createdDate;

    private Long pono;

    private Long pno;


}
