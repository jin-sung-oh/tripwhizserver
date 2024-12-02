package com.example.demo.order.dto;

import com.example.demo.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderProductDTO {

    private ProductDTO product;
    private int amount;

    }



