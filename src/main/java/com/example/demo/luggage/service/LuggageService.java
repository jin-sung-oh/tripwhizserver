package com.example.demo.luggage.service;

import com.example.demo.fcm.dto.FCMRequestDTO;
import com.example.demo.fcm.service.FCMService;
import com.example.demo.luggage.dto.LuggageDTO;
import com.example.demo.luggage.entity.Luggage;
import com.example.demo.luggage.entity.LuggageStatus;
import com.example.demo.luggage.repository.LuggageRepository;
import com.example.demo.member.domain.Member;
import com.example.demo.member.repository.MemberRepository;
import com.example.demo.qrcode.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class LuggageService {

    @Autowired
    private LuggageRepository luggageRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FCMService fcmService;

    @Autowired
    private QRService qrService;

    @Value("${com.example.upload.storagepath}")
    private String storagePath;

    @Value("${com.example.upload.movingpath}")
    private String movingPath;

    public void saveLuggage(LuggageDTO luggageDTO) {
        // 사용자 이메일로 Member 조회
        Member member = memberRepository.findByEmail(luggageDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Member not found with email: " + luggageDTO.getEmail()));

        // Luggage 생성
        Luggage luggage = Luggage.builder()
                .startLat(luggageDTO.getStartPoint().getLat())
                .startLng(luggageDTO.getStartPoint().getLng())
                .endLat(luggageDTO.getEndPoint().getLat())
                .endLng(luggageDTO.getEndPoint().getLng())
                .member(member)
                .status(LuggageStatus.PENDING)
                .build();

        luggageRepository.save(luggage);

        // 점주에게 FCM 알림 전송
        String storeOwnerToken = getStoreOwnerToken();
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(storeOwnerToken)
                .title("New Luggage Request")
                .body("A new luggage request has been submitted by " + luggageDTO.getEmail())
                .build());
    }

    public void approveLuggage(Long luggageId, boolean isStorage) {
        // Luggage 조회
        Luggage luggage = luggageRepository.findById(luggageId)
                .orElseThrow(() -> new RuntimeException("Luggage not found with id: " + luggageId));

        // 상태 변경
        luggage.setStatus(LuggageStatus.APPROVED);

        // QR 코드 생성
        try {
            String qrPath = isStorage
                    ? qrService.generateQRCode(String.valueOf(luggageId), "STORAGE")
                    : qrService.generateQRCode(String.valueOf(luggageId), "MOVING");

            if (isStorage) {
                luggage.setStorageQrCodePath(qrPath); // 보관 QR 코드 경로 저장
            } else {
                luggage.setMovingQrCodePath(qrPath); // 이동 QR 코드 경로 저장
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate QR code for luggage: " + luggageId, e);
        }

        luggageRepository.save(luggage);

        // 사용자에게 FCM 알림 전송
        String userToken = getUserToken(luggage.getMember().getEmail());
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(userToken)
                .title("Luggage Approved")
                .body("Your luggage request has been approved.")
                .build());
    }

    public void rejectLuggage(Long luggageId) {
        // Luggage 조회
        Luggage luggage = luggageRepository.findById(luggageId)
                .orElseThrow(() -> new RuntimeException("Luggage not found with id: " + luggageId));

        // 상태 변경
        luggage.setStatus(LuggageStatus.REJECTED);
        luggageRepository.save(luggage);

        // 사용자에게 FCM 알림 전송
        String userToken = getUserToken(luggage.getMember().getEmail());
        fcmService.sendMessage(FCMRequestDTO.builder()
                .token(userToken)
                .title("Luggage Request Rejected")
                .body("Your luggage request has been rejected.")
                .build());
    }

    private String getStoreOwnerToken() {
        // 점주 FCM 토큰을 가져오는 로직 (데이터베이스 또는 환경 변수 기반)
        return "STORE_OWNER_FCM_TOKEN"; // 실제 점주 토큰으로 대체
    }

    private String getUserToken(String email) {
        // 사용자 FCM 토큰을 가져오는 로직 (데이터베이스 또는 별도 저장소에서 조회)
        return "USER_FCM_TOKEN"; // 실제 사용자 토큰으로 대체
    }
}
