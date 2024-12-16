package com.tripwhiz.tripwhizadminback.stock.dto;

import com.tripwhiz.tripwhizadminback.stock.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StockDTO {

    private Long pno;
    private String pname;
    private Integer quantity;
    private Integer price;


    // 재고를 엔티티에서 DTO로 변환하는 메서드
    public static StockDTO fromEntity(Stock stock) {
        return StockDTO.builder()
                .pno(stock.getProduct().getPno())
                .quantity(stock.getQuantity())
                .pname(stock.getProduct().getPname()) // Product의 이름 포함
                .build();
    }
}
