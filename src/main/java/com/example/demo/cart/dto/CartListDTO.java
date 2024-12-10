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

    private String email;

    private Long bno;

    private Long pno;

    private String pname;

    private int price;

    private int qty;

    private boolean delFlag;

}
