package com.example.demo.store.dto.SpotDTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpotReadDTO {
    private Long spno;
    private String spotname;
    private String address;
    private String tel;
}
