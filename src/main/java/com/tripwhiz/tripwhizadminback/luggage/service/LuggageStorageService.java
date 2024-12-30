package com.tripwhiz.tripwhizadminback.luggage.service;

import com.tripwhiz.tripwhizadminback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizadminback.fcm.service.FCMService;
import com.tripwhiz.tripwhizadminback.luggage.dto.LuggageStorageDTO;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorage;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageStorageStatus;
import com.tripwhiz.tripwhizadminback.luggage.repository.LuggageStorageRepository;
import com.tripwhiz.tripwhizadminback.qrcode.service.QRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class LuggageStorageService {

    private final LuggageStorageRepository luggageStorageRepository;
    private final QRService qrService;
    private final FCMService fcmService;

    public void processLuggageStorage(LuggageStorage luggageStorage) {
        log.info("새로운 수화물 보관 요청 처리 시작: {}", luggageStorage);

        // DB 저장
        luggageStorageRepository.save(luggageStorage);

        // QR 코드 생성
        try {
            String qrText = "StorageSpot: " + luggageStorage.getStorageSpot().getSpno() +
                    ", Email: " + luggageStorage.getEmail() +
                    ", StorageDate: " + luggageStorage.getStorageDate();
            String qrCodePath = qrService.generateQRCode(qrText, "STORAGE", "STORAGE-" + luggageStorage.getLsno());
            luggageStorage.setQrCodePath(qrCodePath);

            luggageStorageRepository.save(luggageStorage);
        } catch (Exception e) {
            log.error("QR 코드 생성 실패: {}", e.getMessage());
            throw new RuntimeException("QR 코드 생성에 실패했습니다.");
        }

        // FCM 알림 전송
        sendAdminNotification("새로운 수화물 보관 요청", "새로운 요청이 접수되었습니다. 요청을 확인하세요.");
    }

    public LuggageStorageDTO getLuggageStorageDetails(Long lsno) {
        LuggageStorage luggageStorage = luggageStorageRepository.findById(lsno)
                .orElseThrow(() -> new IllegalArgumentException("해당 수화물 보관 내역을 찾을 수 없습니다: " + lsno));

        return LuggageStorageDTO.toDTO(luggageStorage);
    }

    public List<LuggageStorageDTO> getAllLuggageStorages() {
        return luggageStorageRepository.findAll().stream()
                .map(LuggageStorageDTO::toDTO)
                .toList();
    }

    public void updateStorageStatus(Long lsno, LuggageStorageStatus newStatus) {
        LuggageStorage luggageStorage = luggageStorageRepository.findById(lsno)
                .orElseThrow(() -> new IllegalArgumentException("해당 수화물 보관 내역을 찾을 수 없습니다: " + lsno));

        luggageStorage.setStatus(newStatus);
        luggageStorageRepository.save(luggageStorage);

        log.info("수화물 상태 업데이트: ID={}, 새로운 상태={}", lsno, newStatus);
    }

    private void sendAdminNotification(String title, String body) {
        FCMRequestDTO request = FCMRequestDTO.builder()
                .token("ejNdaRA-F63OJWcgxg9Kgd:APA91bExX1NC-_ONgAOLw3-nIw7aam6UoOyu3xm5WDroQ4_ixpOshWpTL9OZYXl9RGByU1N5WK5t3L5e40AY5LEhDRxA3Cq4PnDpVc7xntPwzzizbZQp0ic") // 실제 FCM 토큰으로 교체 필요
                .title(title)
                .body(body)
                .build();

        try {
            fcmService.sendMessage(request);
        } catch (Exception e) {
            log.error("FCM 알림 전송 실패: {}", e.getMessage());
        }
    }
}