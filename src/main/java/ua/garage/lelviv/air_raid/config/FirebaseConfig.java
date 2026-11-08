package ua.garage.lelviv.air_raid.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import ua.garage.lelviv.air_raid.exception.FirebaseException;

@Configuration
@Validated
@Slf4j
@ConditionalOnProperty(name = "app.firebase.enabled", havingValue = "true")
public class FirebaseConfig {
    @NotNull(message = "Firebase credentials are not set")
    @NotEmpty(message = "Firebase credentials are empty")
    @Size(min = 20, message = "Firebase credentials are too short")
    @Value("${app.firebase.credentials.json}")
    private String firebaseCredentialsJson;

    @NotNull
    @Value("${app.firebase.enabled}")
    private Boolean enabled;

    @Bean
    public FirebaseMessaging messaging(FirebaseApp app) {
        return FirebaseMessaging.getInstance(app);
    }

    @Bean
    public FirebaseApp initialize() {
        try {
            InputStream serviceAccount =
                    new ByteArrayInputStream(firebaseCredentialsJson.getBytes(StandardCharsets.UTF_8));
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
