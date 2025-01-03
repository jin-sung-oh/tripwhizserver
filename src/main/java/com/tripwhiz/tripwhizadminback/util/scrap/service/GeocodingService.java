package com.tripwhiz.tripwhizadminback.util.scrap.service;

import com.tripwhiz.tripwhizadminback.util.UrlExpander;
import com.tripwhiz.tripwhizadminback.util.scrap.dto.GeocodingResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            // 2. URL에서 위도와 경도 추출 시도
            try {
                return extractLatLng(expandedUrl);
            } catch (Exception e) {
                log.warn("URL에서 유효한 위도/경도를 찾을 수 없음. URL을 건너뜀: {}", expandedUrl);
                // NaN 값을 반환하여 유효하지 않은 좌표를 표시하거나 건너뜀
                return new double[]{Double.NaN, Double.NaN}; // NaN을 통해 유효하지 않은 값을 반환
            }
        } catch (Exception e) {
            log.error("URL 처리 실패: {}", shortUrl, e);
            throw new RuntimeException("URL 처리 중 오류 발생: " + shortUrl, e);
        }
    }

    // URL에서 위도와 경도를 추출하는 메서드
    private double[] extractLatLng(String expandedUrl) {
        try {
            Pattern pattern = Pattern.compile("(-?\\d+\\.\\d+),(-?\\d+\\.\\d+)");
            Matcher matcher = pattern.matcher(expandedUrl);

            if (matcher.find()) {
                double latitude = Double.parseDouble(matcher.group(1));
                double longitude = Double.parseDouble(matcher.group(2));
                log.info("Extracted Latitude: {}, Longitude: {}", latitude, longitude);
                return new double[]{latitude, longitude};
            } else {
                throw new RuntimeException("Failed to match latitude and longitude in URL: " + expandedUrl);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract latitude and longitude from URL: " + expandedUrl, e);
        }
    }

    // Geocoding API 호출 메서드
    private double[] fetchLatLngFromGeocodingApi(String expandedUrl) {
        String geocodingApiUrl = UriComponentsBuilder
                .fromHttpUrl("https://maps.googleapis.com/maps/api/geocode/json")
                .queryParam("address", expandedUrl)
                .queryParam("key", apiKey)
                .toUriString();

        GeocodingResponse response = restTemplate.getForObject(geocodingApiUrl, GeocodingResponse.class);

        if (response == null || !"OK".equals(response.getStatus()) || response.getResults().isEmpty()) {
            throw new RuntimeException("Failed to retrieve geolocation from Geocoding API for URL: " + expandedUrl);
        }

        double latitude = response.getResults().get(0).getGeometry().getLocation().getLat();
        double longitude = response.getResults().get(0).getGeometry().getLocation().getLng();
        return new double[]{latitude, longitude};
    }

}
