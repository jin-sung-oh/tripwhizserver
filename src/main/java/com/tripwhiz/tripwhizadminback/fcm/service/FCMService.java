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
        validateRequest(fcmRequestDTO);

        Notification notification = Notification.builder()
                .setBody(fcmRequestDTO.getBody())
                .setTitle(fcmRequestDTO.getTitle())
                .build();

        Message message = Message.builder()
                .setToken(fcmRequestDTO.getToken())
                .setNotification(notification)
                .build();

        try {
            firebaseMessaging.send(message);
            log.info("FCM Message Sent: {}", fcmRequestDTO);
        } catch (FirebaseMessagingException e) {
            log.error("Failed to send FCM message: {}", e.getMessage(), e);
            throw new FCMMessageException(e.getMessage());
        }
    }

    private void validateRequest(FCMRequestDTO fcmRequestDTO) {
        if (fcmRequestDTO == null) {
            throw new FCMMessageException("FCMRequestDTO is null");
        }
        if (fcmRequestDTO.getToken() == null || fcmRequestDTO.getToken().isEmpty()) {
            throw new FCMMessageException("FCMRequestDTO token is null or empty");
        }
        if (fcmRequestDTO.getTitle() == null || fcmRequestDTO.getTitle().isEmpty()) {
            throw new FCMMessageException("FCMRequestDTO title is null or empty");
        }
    }
}