package com.tripwhiz.tripwhizadminback.product.dto;

import com.tripwhiz.tripwhizadminback.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Long pno;
    private String pname;
    private int price;

    public static ProductDTO fromEntity(Product product) {
        return ProductDTO.builder()
                .pno(product.getPno())
                .pname(product.getPname())
                .price(product.getPrice())
                .build();
    }

}