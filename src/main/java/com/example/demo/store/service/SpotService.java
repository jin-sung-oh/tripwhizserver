package com.example.demo.store.service;

import com.example.demo.store.domain.Spot;
import com.example.demo.store.dto.SpotDTO.SpotModifyDTO;
import com.example.demo.store.repository.SpotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class SpotService {
    private final SpotRepository spotRepository;

    public Spot read(Long spno) {
        return spotRepository.findById(spno).orElse(null);
    }

    public List<Spot> list() {
        return spotRepository.findAll();
    }

    public Spot add(Spot spot) {
        return spotRepository.save(spot);
    }

    public void modify(Long spno, SpotModifyDTO modifyDTO) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot not found"));
        spot.setSpotname(modifyDTO.getSpotname());
        spot.setAddress(modifyDTO.getAddress());
        spot.setTel(modifyDTO.getTel());
        spotRepository.save(spot);
    }

    public void softDelete(Long spno) {
        Spot spot = spotRepository.findById(spno)
                .orElseThrow(() -> new IllegalArgumentException("Spot not found"));
        spot.setDelFlag(true);
        spotRepository.save(spot);
    }
}