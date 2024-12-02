package com.example.demo.luggage.dto;

import lombok.Data;

@Data
public class LuggageDTO {

    private Point startPoint;
    private Point endPoint;
    private String email;

    @Data
    public static class Point {
        private Double lat;
        private Double lng;
    }
}
