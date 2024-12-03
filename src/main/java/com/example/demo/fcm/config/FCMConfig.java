package com.example.demo.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class FCMConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        String firebaseConfigPath = System.getenv("FIREBASE_CONFIG_PATH");

        if (firebaseConfigPath == null || firebaseConfigPath.isEmpty()) {
            throw new IllegalArgumentException("환경 변수 'FIREBASE_CONFIG_PATH'가 설정되지 않았습니다. Firebase 서비스 계정 키 파일 경로를 지정하세요.");
        }

        try (FileInputStream serviceAccount = new FileInputStream(firebaseConfigPath)) {
            FirebaseApp firebaseApp = null;
            List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

            if (firebaseApps != null && !firebaseApps.isEmpty()) {
                for (FirebaseApp existingApp : firebaseApps) {
                    if (FirebaseApp.DEFAULT_APP_NAME.equals(existingApp.getName())) {
                        firebaseApp = existingApp;
                        break;
                    }
                }
            } else {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                firebaseApp = FirebaseApp.initializeApp(options);
            }

            return FirebaseMessaging.getInstance(firebaseApp);
        }
    }
}
