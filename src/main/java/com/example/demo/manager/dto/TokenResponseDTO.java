package com.example.demo.manager.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
