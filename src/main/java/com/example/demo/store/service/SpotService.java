package com.example.demo.store.service;

import com.example.demo.store.domain.Spot;
import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.store.dto.SpotDTO.SpotDTO;
import com.example.demo.store.repository.SpotRepository;
import com.example.demo.manager.repository.StoreOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
                .tel(spot.getTel())
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
                        .tel(spot.getTel())
                        .build())
                .collect(Collectors.toList());
    }

    // 새로운 Spot 추가
    public SpotDTO add(SpotDTO spotDTO) {
        StoreOwner storeOwner = storeOwnerRepository.findById(spotDTO.getSno())
                .orElseThrow(() -> new IllegalArgumentException("Store Owner not found."));

        Spot spot = Spot.builder()
                .storeowner(storeOwner)
                .tel(spotDTO.getTel())
                .spotname(spotDTO.getSpotname())
                .address(spotDTO.getAddress())
                .build();

        spotRepository.save(spot);

        spotDTO.setSpno(spot.getSpno());

        return spotDTO;
    }

    // 기존 Spot 수정
    public void modify(Long spno, SpotDTO modifyDTO) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        spot.setSpotname(modifyDTO.getSpotname());
        spot.setAddress(modifyDTO.getAddress());
        spot.setTel(modifyDTO.getTel());
        spotRepository.save(spot);
    }

    // Spot 삭제
    public void delete(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        spotRepository.delete(spot);
    }
}
