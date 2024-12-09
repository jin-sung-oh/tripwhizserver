package com.example.demo.util.scrap.service;

import com.example.demo.util.UrlExpander;
import com.example.demo.util.scrap.dto.GeocodingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.demo.util.scrap.dto.GeocodingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class GeocodingService {

    @Value("${google.maps.api.key}")
    private String apiKey; // Google API 키를 application.properties에 설정

    private final RestTemplate restTemplate;

    public double[] getLatLng(String shortUrl) {
        try {
            // 1. URL 확장
            String expandedUrl = UrlExpander.expandShortUrl(shortUrl);
            log.info("Expanded URL: {}", expandedUrl);

            // 2. URL 유형 판단
            if (expandedUrl.contains("@")) {
                // 2-1. "@<위도>,<경도>"를 포함한 경우
                try {
                    return extractLatLng(expandedUrl);
                } catch (Exception e) {
                    log.warn("Failed to extract lat/lng from URL: {}, falling back to Geocoding API", expandedUrl);
                }
            }

            // 2-2. Geocoding API로 처리
            log.info("Using Geocoding API for URL: {}", expandedUrl);
            String geocodingApiUrl = UriComponentsBuilder
                    .fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                    .queryParam("address", expandedUrl)
                    .queryParam("key", apiKey)
                    .toUriString();

            GeocodingResponse response = restTemplate.getForObject(geocodingApiUrl, GeocodingResponse.class);

            if (response != null && response.getStatus().equals("OK")) {
                double latitude = response.getResults().get(0).getGeometry().getLocation().getLat();
                double longitude = response.getResults().get(0).getGeometry().getLocation().getLng();
                return new double[]{latitude, longitude};
            } else {
                throw new RuntimeException("Failed to retrieve geolocation from Geocoding API for URL: " + expandedUrl);
            }
        } catch (IOException e) {
            log.error("Error expanding short URL: {}", shortUrl, e);
            throw new RuntimeException("Failed to expand short URL", e);
        }
    }


    private double[] extractLatLng(String expandedUrl) {
        try {
            // "@위도,경도" 부분 추출
            String latLngPart = expandedUrl.split("@")[1].split(",")[0] + "," + expandedUrl.split("@")[1].split(",")[1];
            String[] parts = latLngPart.split(",");
            double latitude = Double.parseDouble(parts[0].trim());
            double longitude = Double.parseDouble(parts[1].trim());
            return new double[]{latitude, longitude};
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract latitude and longitude from URL: " + expandedUrl, e);
        }
    }
}
