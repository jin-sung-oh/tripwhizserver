package com.tripwhiz.tripwhizadminback.fcm.service;

import com.tripwhiz.tripwhizadminback.fcm.dto.FCMRequestDTO;
import com.tripwhiz.tripwhizadminback.fcm.exceptions.FCMMessageException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Log4j2
public class FCMService {

    private final FirebaseMessaging firebaseMessaging;

    public void sendMessage(FCMRequestDTO fcmRequestDTO) {
        try {
            // 요청 유효성 검사
            validateRequest(fcmRequestDTO);

            // 알림 생성
            Notification notification = Notification.builder()
                    .setBody(fcmRequestDTO.getBody())
                    .setTitle(fcmRequestDTO.getTitle())
                    .build();

            // FCM 메시지 빌드
            Message message = Message.builder()
                    .setToken(fcmRequestDTO.getToken())
                    .setNotification(notification)
                    .build();

            // 메시지 전송
            String response = firebaseMessaging.send(message);
            log.info("FCM 메시지 전송 성공: {}", response);

        } catch (IllegalArgumentException e) {
            log.error("유효하지 않은 요청: {}", e.getMessage());
            throw new FCMMessageException("유효하지 않은 요청: " + e.getMessage());
        } catch (FirebaseMessagingException e) {
            log.error("FCM 메시지 전송 실패: {}", e.getMessage(), e);
            throw new FCMMessageException("FCM 메시지 전송 실패: " + e.getMessage());
        } catch (Exception e) {
            log.error("알 수 없는 에러 발생: {}", e.getMessage(), e);
            throw new FCMMessageException("알 수 없는 에러 발생: " + e.getMessage());
        }
    }

    private void validateRequest(FCMRequestDTO fcmRequestDTO) {
        if (fcmRequestDTO == null) {
            throw new IllegalArgumentException("FCMRequestDTO가 null입니다.");
        }
        if (fcmRequestDTO.getToken() == null || fcmRequestDTO.getToken().isEmpty()) {
            throw new IllegalArgumentException("FCMRequestDTO 토큰이 null이거나 비어 있습니다.");
        }
        if (fcmRequestDTO.getTitle() == null || fcmRequestDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException("FCMRequestDTO 제목이 null이거나 비어 있습니다.");
        }
        if (fcmRequestDTO.getBody() == null || fcmRequestDTO.getBody().isEmpty()) {
            throw new IllegalArgumentException("FCMRequestDTO 본문이 null이거나 비어 있습니다.");
        }
    }
}