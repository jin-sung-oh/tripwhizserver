package com.tripwhiz.tripwhizadminback.admin.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
