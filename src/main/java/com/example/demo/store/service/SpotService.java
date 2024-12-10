package com.example.demo.store.service;

import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.repository.StoreOwnerRepository;
import com.example.demo.store.domain.Spot;
import com.example.demo.store.dto.SpotDTO;
import com.example.demo.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class SpotService {

    private final SpotRepository spotRepository;
    private final StoreOwnerRepository storeOwnerRepository;

    // 특정 Spot 읽기
    public SpotDTO read(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        StoreOwner storeOwner = spot.getStoreowner();

        return SpotDTO.builder()
                .spno(spot.getSpno())
                .spotname(spot.getSpotname())
                .address(spot.getAddress())
                .url(spot.getUrl())
//                .tel(spot.getTel())
                .latitude(spot.getLatitude()) // 위도 추가
                .longitude(spot.getLongitude()) // 경도 추가
                .sno(storeOwner.getSno())
                .sname(storeOwner.getSname())
                .build();
    }

    // 모든 Spot 리스트 반환
    public List<SpotDTO> list() {
        return spotRepository.findAll().stream()
                .map(spot -> SpotDTO.builder()
                        .spno(spot.getSpno())
                        .spotname(spot.getSpotname())
                        .address(spot.getAddress())
                        .url(spot.getUrl())
//                        .tel(spot.getTel())
                        .latitude(spot.getLatitude()) // 위도 추가
                        .longitude(spot.getLongitude()) // 경도 추가
                        .sno(spot.getStoreowner().getSno())
                        .sname(spot.getStoreowner().getSname())
                        .build())
                .collect(Collectors.toList());
    }

    // 새로운 Spot 추가
    public SpotDTO add(SpotDTO spotDTO) {
        StoreOwner storeOwner = storeOwnerRepository.findById(spotDTO.getSno())
                .orElseThrow(() -> new IllegalArgumentException("Store Owner not found."));

        Spot spot = Spot.builder()
                .storeowner(storeOwner)
                .spotname(spotDTO.getSpotname())
                .address(spotDTO.getAddress())
                .url(spotDTO.getUrl())
//                .tel(spotDTO.getTel())
                .latitude(spotDTO.getLatitude()) // 위도 추가
                .longitude(spotDTO.getLongitude()) // 경도 추가
                .delFlag(false) // 기본값 설정
                .build();

        spotRepository.save(spot);

        spotDTO.setSpno(spot.getSpno());
        return spotDTO;
    }

    // Spot 수정
    public SpotDTO modify(Long spno, SpotDTO modifyDTO) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot not found."));

        spot.setSpotname(modifyDTO.getSpotname());
        spot.setAddress(modifyDTO.getAddress());
        spot.setUrl(modifyDTO.getUrl());
//        spot.setTel(modifyDTO.getTel());
        spot.setLatitude(modifyDTO.getLatitude()); // 위도 수정
        spot.setLongitude(modifyDTO.getLongitude()); // 경도 수정

        Spot updatedSpot = spotRepository.save(spot);

        return SpotDTO.builder()
                .spno(updatedSpot.getSpno())
                .spotname(updatedSpot.getSpotname())
                .address(updatedSpot.getAddress())
                .url(updatedSpot.getUrl())
//                .tel(updatedSpot.getTel())
                .latitude(updatedSpot.getLatitude()) // 위도 추가
                .longitude(updatedSpot.getLongitude()) // 경도 추가
                .sno(updatedSpot.getStoreowner().getSno())
                .sname(updatedSpot.getStoreowner().getSname())
                .build();
    }

    // Spot 삭제
    public void delete(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        spotRepository.delete(spot);
    }
}
