package com.tripwhiz.tripwhizadminback.util.scrap.service;

import com.tripwhiz.tripwhizadminback.storeowner.entity.StoreOwner;
import com.tripwhiz.tripwhizadminback.storeowner.repository.StoreOwnerRepository;
import com.tripwhiz.tripwhizadminback.spot.entity.Spot;
import com.tripwhiz.tripwhizadminback.spot.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class ScrapingService {

    private final SpotRepository spotRepository;
    private final StoreOwnerRepository storeOwnerRepository;
    private final GeocodingService geocodingService;

    private static final String SpotURL = "https://emart24.com.my/locations/#2";

    // Storeowner 레코드에서 랜덤으로 하나 선택하는 메서드
    private StoreOwner getRandomStoreowner() {
        List<StoreOwner> storeowners = storeOwnerRepository.findAll();
        if (storeowners.isEmpty()) {
            throw new IllegalStateException("No storeowners available in the database.");
        }

        // 랜덤으로 Storeowner 선택
        int randomIndex = new Random().nextInt(storeowners.size());
        return storeowners.get(randomIndex);
    }

    public List<Spot> getSpotList() throws IOException {
        List<Spot> spotList = new ArrayList<>();
        Document document = Jsoup.connect(SpotURL).get();

        if (document == null) {
            log.error("Failed to connect to URL: {}", SpotURL);
            return spotList; // 문서를 가져오지 못한 경우 빈 리스트 반환
        }

        log.info("Successfully connected to {}", SpotURL);

        // 지점 정보가 포함된 모든 컨테이너를 선택
        Elements spotContainers = document.select(".et_pb_blurb_container");

        if (spotContainers.isEmpty()) {
            log.warn("No spot containers found at {}", SpotURL);
            return spotList; // 아무 데이터도 없으면 빈 리스트 반환
        }

        // 각 지점 처리
        for (Element spotContainer : spotContainers) {
            try {
                // 지점명 추출
                String spotName = spotContainer.select(".et_pb_module_header span").text().trim();

                // 주소에서 loc-title 제거 후 텍스트만 추출
                Element addressElement = spotContainer.selectFirst(".loc-address");
                String address = "";
                if (addressElement != null) {
                    addressElement.select(".loc-title").remove(); // loc-title 제거
                    address = addressElement.text().trim();
                }

                // URL 추출
                String url = spotContainer.select(".loc-direction a").attr("abs:href");

                // 데이터 유효성 검증
                if (spotName.isEmpty() || address.isEmpty() || url.isEmpty()) {
                    log.warn("Invalid data found - SpotName: {}, Address: {}, URL: {}", spotName, address, url);
                    continue; // 잘못된 데이터는 건너뜀
                }

                // Google API를 통해 위도와 경도 추출
                double[] latLng = geocodingService.getLatLng(url);
                double latitude = latLng[0];
                double longitude = latLng[1];

                // 위도와 경도 값 검증
                if (Double.isNaN(latitude) || Double.isNaN(longitude)) {
                    log.warn("Invalid latitude/longitude for SpotName: {}, skipping...", spotName);
                    continue; // 유효하지 않은 좌표는 건너뜀
                }

                // 랜덤으로 Storeowner 가져오기
                StoreOwner storeowner = getRandomStoreowner();

                // Spot 객체 생성
                Spot spot = Spot.builder()
                        .spotname(spotName)
                        .address(address)
                        .url(url)
                        .latitude(latitude)
                        .longitude(longitude)
                        .storeowner(storeowner)
                        .build();

                spotList.add(spot);
                log.info("Successfully added Spot: {}", spot);

                // 데이터베이스에 저장
                spotRepository.save(spot);

            } catch (Exception e) {
                log.error("Error processing spotContainer: {}", spotContainer, e);
            }
        }

        return spotList;
    }
}

