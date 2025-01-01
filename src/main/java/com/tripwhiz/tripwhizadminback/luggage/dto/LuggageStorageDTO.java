package com.tripwhiz.tripwhizadminback.luggage.dto;

import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorageStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LuggageStorageDTO {
    private Long lsno;
    private String storageSpotName;
    private String email;
    private String qrCodePath;
    private LocalDateTime storageDate;
    private LocalDateTime storedUntil;
    private LuggageStorageStatus status;

    public static LuggageStorageDTO toDTO(LuggageStorage luggageStorage) {
        return LuggageStorageDTO.builder()
                .lsno(luggageStorage.getLsno())
                .storageSpotName(luggageStorage.getStorageSpot().getSpotname())
                .email(luggageStorage.getEmail())
                .qrCodePath(luggageStorage.getQrCodePath())
                .storageDate(luggageStorage.getStorageDate())
                .storedUntil(luggageStorage.getStoredUntil())
                .status(luggageStorage.getStatus())
                .build();
    }
}
