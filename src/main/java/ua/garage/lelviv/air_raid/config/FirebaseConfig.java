package ua.garage.lelviv.air_raid.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.garage.lelviv.air_raid.exception.FirebaseException;

@Configuration
@Slf4j
@ConditionalOnProperty(name = "app.firebase.enabled", havingValue = "true")
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebaseConfig {
    @Bean
    public FirebaseMessaging messaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }

    @Bean
    public FirebaseApp initialize(FirebaseProperties firebaseProperties) {
        try {
            InputStream serviceAccount =
                    new ByteArrayInputStream(firebaseProperties.credentials().getBytes(StandardCharsets.UTF_8));
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                return FirebaseApp.initializeApp(options);
            } else {
                return FirebaseApp.getInstance();
            }
        } catch (Exception e) {
            log.error("Failed to initialize Firebase admin sdk!");
            throw new FirebaseException(e.toString());
        }
    }
}
