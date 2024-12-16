package com.tripwhiz.tripwhizadminback.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FCMRequestDTO {

    private String token;
    private String title;
    private String body;

}
