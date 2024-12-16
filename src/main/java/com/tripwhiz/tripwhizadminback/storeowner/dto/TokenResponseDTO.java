package com.tripwhiz.tripwhizadminback.storeowner.dto;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
