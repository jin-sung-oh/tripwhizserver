package com.tripwhiz.tripwhizadminback.util.scrap.dto;

import lombok.Data;

import java.util.List;

@Data
public class GeocodingResponse {
    private List<Result> results;
    private String status;

    @Data
    public static class Result {
        private Geometry geometry;
    }

    @Data
    public static class Geometry {
        private Location location;
    }

    @Data
    public static class Location {
        private double lat; // 위도
        private double lng; // 경도
    }
}
