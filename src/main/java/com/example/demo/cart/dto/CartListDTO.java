package com.example.demo.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartListDTO {

    private Long bno;

    private Long mno;

    private Long pno;

    private int totalQty;

    private int totalPrice;

}
