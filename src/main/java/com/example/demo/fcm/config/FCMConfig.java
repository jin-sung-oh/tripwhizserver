package com.example.demo.fcm.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class FCMConfig {

    @Bean
    FirebaseMessaging firebaseMessaging() throws IOException {

        ClassPathResource resource = new ClassPathResource("firebase/jin1107-c14a2-firebase-adminsdk-vvtqr-c688c2c6b4.json");

        InputStream inputStream = resource.getInputStream();

        FirebaseApp firebaseApp = null;
        List<FirebaseApp> firebaseApps = FirebaseApp.getApps();

        if( firebaseApps != null &&  firebaseApps.size() > 0) {

            for(FirebaseApp firebaseApp1 : firebaseApps) {
                if(firebaseApp1.getName().equals(FirebaseApp.DEFAULT_APP_NAME)) {
                    firebaseApp = firebaseApp1;
                }
            }//end for

        }else {
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream)).build();

            firebaseApp = FirebaseApp.initializeApp(firebaseOptions);
        }

        return FirebaseMessaging.getInstance(firebaseApp);
    }
}

