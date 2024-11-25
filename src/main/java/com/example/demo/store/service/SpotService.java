package com.example.demo.store.service;

import com.example.demo.common.dto.PageRequestDTO;
import com.example.demo.common.dto.PageResponseDTO;
import com.example.demo.manager.entity.StoreOwner;
import com.example.demo.manager.repository.StoreOwnerRepository;
import com.example.demo.store.domain.Spot;
import com.example.demo.store.dto.SpotDTO.SpotListDTO;
import com.example.demo.store.dto.SpotDTO.SpotModifyDTO;
import com.example.demo.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class SpotService {

    private final SpotRepository spotRepository;
    private final StoreOwnerRepository storeOwnerRepository;

    public Spot read(Long spno) {
        return spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
    }

    public PageResponseDTO<SpotListDTO> list(PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());
        Page<Spot> result = spotRepository.findAll(pageable);

        List<SpotListDTO> dtoList = result.getContent()
                .stream()
                .map(spot -> SpotListDTO.builder()
                        .spno(spot.getSpno())
                        .spotname(spot.getSpotname())
                        .address(spot.getAddress())
                        .tel(spot.getTel())
                        .build())
                .collect(Collectors.toList());

        return PageResponseDTO.<SpotListDTO>withAll()
                .dtoList(dtoList)
                .pageRequestDTO(pageRequestDTO)
                .totalCount(result.getTotalElements())
                .build();
    }

    public Spot add(Spot spot) {
        // SNO 중복 확인
        int sno = spot.getStoreowner().getSno();
        if (storeOwnerRepository.existsById(sno)) {
            throw new IllegalArgumentException("Store Owner with SNO " + sno + " already exists.");
        }

        // StoreOwner 생성 및 저장
        StoreOwner storeOwner = spot.getStoreowner();
        StoreOwner newStoreOwner = new StoreOwner();
        newStoreOwner.setSname(storeOwner.getSname());
        newStoreOwner.setId(storeOwner.getId());
        newStoreOwner.setPw(storeOwner.getPw());
        newStoreOwner.setEmail(storeOwner.getEmail());
        newStoreOwner.setDelFlag(storeOwner.isDelFlag());
        newStoreOwner.setRole(storeOwner.getRole());
        StoreOwner savedStoreOwner = storeOwnerRepository.save(newStoreOwner);

        // Spot에 StoreOwner 설정
        spot.setStoreowner(savedStoreOwner);

        // Spot 저장
        return spotRepository.save(spot);
    }

    public void modify(Long spno, SpotModifyDTO modifyDTO) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        spot.setSpotname(modifyDTO.getSpotname());
        spot.setAddress(modifyDTO.getAddress());
        spot.setTel(modifyDTO.getTel());
        spotRepository.save(spot);
    }

    public void softDelete(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot with ID " + spno + " not found."));
        spot.setDelFlag(true);
        spotRepository.save(spot);
    }
}
