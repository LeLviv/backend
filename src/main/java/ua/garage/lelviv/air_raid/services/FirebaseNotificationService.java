package ua.garage.lelviv.air_raid.services;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.air_raid.config.AirRaidProperties;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@Service
@ConditionalOnProperty(name = "app.firebase.enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class FirebaseNotificationService implements NotificationService {
    private final AirRaidProperties properties;
    private final FirebaseMessaging messaging;

    public void sendNotification(AlertType type, AlertStatus status) {
        if (status == null) {
            log.error("Current status of alert {} is null", type);
            return;
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("type", type.name());
        payload.put("status", status.name());
        payload.put(
                "time",
                LocalDateTime.now(ZoneId.of(properties.timeZone())).format(DateTimeFormatter.ofPattern("HH:mm")));
        Message message =
                Message.builder().setTopic("air_raid_alert").putAllData(payload).build();
        try {
            log.info("Sending notification to Firebase");
            String response = messaging.send(message);
            log.info("Received answer: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Error during FCM sending: {}", e.getMessage());
        }
    }
}
