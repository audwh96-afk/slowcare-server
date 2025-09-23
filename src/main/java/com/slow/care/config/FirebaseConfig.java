package com.slow.care.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try { 
            // resources 폴더에서 파일 읽기입니다.
            ClassPathResource resource = new ClassPathResource("slowcare-d48ff-firebase-adminsdk-fbsvc-89aef95574.json");
            InputStream serviceAccount = resource.getInputStream();
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            // Firebase 앱이 이미 초기화되었는지 확인
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase 초기화 성공!");
            }

        } catch (IOException e) {
            System.err.println("Firebase 초기화 실패: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
