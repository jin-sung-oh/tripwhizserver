package com.tripwhiz.tripwhizadminback.luggage.service;

import com.tripwhiz.tripwhizadminback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizadminback.fcm.service.FCMService;
import com.tripwhiz.tripwhizadminback.luggage.dto.LuggageStorageDTO;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorageStatus;
import com.tripwhiz.tripwhizadminback.luggage.repository.LuggageStorageRepository;
import com.tripwhiz.tripwhizadminback.qrcode.service.QRService;
import com.tripwhiz.tripwhizadminback.spot.entity.Spot;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class LuggageStorageService {

    private final LuggageStorageRepository luggageStorageRepository;
    private final QRService qrService;
    private final FCMService fcmService;

    public LuggageStorageDTO createLuggageStorage(LuggageStorage luggageStorage) {

        Spot storageSpot = Spot.builder()
                .spno(luggageStorage.getStorageSpot().getSpno())
                .build();

        LuggageStorage targetEntity = LuggageStorage.builder()
                .storageSpot(storageSpot)
                .email(luggageStorage.getEmail())
                .build();

        log.info("=================================");
        log.info("보관 데이터 저장 시작: {}", targetEntity);
        log.info("=================================");

        luggageStorageRepository.save(targetEntity);

        // QR 코드 생성
        try {
            String qrText = "storage: " + luggageStorage.getStorageSpot().getSpno() +
                    ", Email: " + luggageStorage.getEmail() +
                    ", storageDate: " + luggageStorage.getStorageDate();

            String qrCodePath = qrService.generateQRCode(qrText, "STORAGE", "STORAGE-" + luggageStorage.getLsno());
            luggageStorage.setQrCodePath(qrCodePath);
            luggageStorageRepository.save(luggageStorage);
        } catch (Exception e) {
            throw new RuntimeException("QR 코드를 생성하지 못했습니다: " + luggageStorage.getLsno(), e);
        }

        // 유저 알림 발송
        sendUserNotification(luggageStorage.getEmail(), "수화물 보관 신청 완료",
                "수화물 보관 신청이 성공적으로 접수되었습니다. QR 코드가 준비되었습니다.");

        return LuggageStorageDTO.toDTO(luggageStorage);
    }

    public LuggageStorageDTO getLuggageStorageDetails(Long lsno) {
        LuggageStorage luggageStorage = luggageStorageRepository.findById(lsno)
                .orElseThrow(() -> new IllegalArgumentException("해당 보관 내역을 찾을 수 없습니다."));
        return LuggageStorageDTO.toDTO(luggageStorage);
    }

    public List<LuggageStorageDTO> getAllLuggageStorages() {
        return luggageStorageRepository.findAll().stream()
                .map(LuggageStorageDTO::toDTO)
                .collect(Collectors.toList());
    }

    public void updateStorageStatus(Long lsno, LuggageStorageStatus newStatus) {
        LuggageStorage luggageStorage = luggageStorageRepository.findById(lsno)
                .orElseThrow(() -> new IllegalArgumentException("해당 보관 내역을 찾을 수 없습니다."));
        luggageStorage.setStatus(newStatus);

        String statusMessage = switch (newStatus) {
            case APPROVED -> "수화물 보관이 승인되었습니다.";
            case REJECTED -> "수화물 보관 요청이 거절되었습니다.";
            case STORED -> "수화물이 보관되었습니다.";
            default -> "수화물 보관 상태가 업데이트되었습니다.";
        };

        sendUserNotification(luggageStorage.getEmail(), "수화물 보관 상태 업데이트", statusMessage);

        luggageStorageRepository.save(luggageStorage);
    }

    private void sendUserNotification(String email, String title, String body) {
        String userToken = getUserToken(email);

        FCMRequestDTO request = FCMRequestDTO.builder()
                .token(userToken)
                .title(title)
                .body(body)
                .build();

        fcmService.sendMessage(request);
    }

    private String getUserToken(String email) {
        return "USER_FCM_TOKEN"; // 실제 토큰 조회 로직으로 대체 필요
    }
}
