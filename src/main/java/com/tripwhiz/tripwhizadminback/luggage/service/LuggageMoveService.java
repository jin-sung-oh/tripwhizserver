package com.tripwhiz.tripwhizadminback.luggage.service;

import com.tripwhiz.tripwhizadminback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizadminback.fcm.service.FCMService;
import com.tripwhiz.tripwhizadminback.luggage.dto.LuggageMoveDTO;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageMove;
import com.tripwhiz.tripwhizadminback.luggage.entity.LuggageMoveStatus;
import com.tripwhiz.tripwhizadminback.luggage.repository.LuggageMoveRepository;
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
public class LuggageMoveService {

    private final LuggageMoveRepository luggageMoveRepository;
    private final QRService qrService;
    private final FCMService fcmService;

    // 이동 신청 생성
    public LuggageMoveDTO createLuggageMove(LuggageMove luggageMove) {


        Spot sourceSpot = Spot.builder()
                        .spno(luggageMove.getSourceSpot().getSpno())
                        .build();

        Spot destincationSpot = Spot.builder()
                .spno(luggageMove.getDestinationSpot().getSpno())
                .build();


        LuggageMove targetEntity = LuggageMove.builder()
                .sourceSpot(sourceSpot)
                .destinationSpot(destincationSpot)
                .email(luggageMove.getEmail())
                .build();

        log.info("=================================");
        log.info("=================================");
        log.info(targetEntity);

        log.info("=================================");


        luggageMoveRepository.save(targetEntity);

        // QR 코드 생성
        try {
            String qrText = "Source: " + luggageMove.getSourceSpot().getSpno() +
                    ", Destination: " + luggageMove.getDestinationSpot().getSpno() +
                    ", Email: " + luggageMove.getEmail() +
                    ", Move Date: " + luggageMove.getMoveDate();
            log.info("QR Text: {}", qrText);

            String qrCodePath = qrService.generateQRCode(qrText, "MOVING", "MOVE-" + luggageMove.getLmno());
            luggageMove.setQrCodePath(qrCodePath); // QR 코드 경로 저장
            luggageMoveRepository.save(luggageMove); // 경로 업데이트 후 다시 저장
        } catch (Exception e) {

            e.printStackTrace();
            throw new RuntimeException("QR 코드를 생성하지 못했습니다: " + luggageMove.getLmno(), e);
        }

        return LuggageMoveDTO.toDTO(luggageMove);
    }

    // 이동 상세 정보 조회
    public LuggageMoveDTO getLuggageMoveDetails(Long lmno) {
        LuggageMove luggageMove = luggageMoveRepository.findById(lmno)
                .orElseThrow(() -> new IllegalArgumentException("Move not found"));
        return LuggageMoveDTO.toDTO(luggageMove);
    }

    // 모든 이동 리스트 조회
    public List<LuggageMoveDTO> getAllLuggageMoves() {
        return luggageMoveRepository.findAll().stream()
                .map(LuggageMoveDTO::toDTO)
                .collect(Collectors.toList());
    }

    // 이동 상태 업데이트
    public void updateMoveStatus(Long lmno, LuggageMoveStatus newStatus) {
        LuggageMove luggageMove = luggageMoveRepository.findById(lmno)
                .orElseThrow(() -> new IllegalArgumentException("Move not found"));
        luggageMove.setStatus(newStatus);

        // 상태 업데이트에 따른 알림
        String statusMessage = switch (newStatus) {
            case APPROVED -> "수화물 이동이 승인되었습니다.";
            case IN_TRANSIT -> "수화물이 현재 이동 중입니다.";
            case DELIVERED -> "수화물이 도착했습니다.";
            default -> "수화물 이동 상태가 업데이트되었습니다.";
        };

        sendUserNotification(luggageMove.getEmail(), "수화물 이동 상태 업데이트", statusMessage);

        luggageMoveRepository.save(luggageMove);
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
        return "USER_FCM_TOKEN"; // 임시로 반환
    }
}
