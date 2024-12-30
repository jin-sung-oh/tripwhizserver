package com.tripwhiz.tripwhizadminback.stock.dto;

import com.tripwhiz.tripwhizadminback.stock.entity.PurchaseOrderStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderDTO {
    private long pono;

    private int totalamount;

    private int totalprice;

    private LocalDateTime createdDate;

    private PurchaseOrderStatus status;


}
