package com.example.demo.admin.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
