package ua.garage.lelviv.air_raid;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.air_raid.config.AirRaidProperties;
import ua.garage.lelviv.air_raid.entities.Alert;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirRaidService {
    private final RestClient airRaidProvider;
    private final Cache<AlertType, AlertStatus> airRaidCache;
    private final AirRaidProperties properties;

    public void sendNotification(AlertType type) {
        AlertStatus status = airRaidCache.getIfPresent(type);
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
            String response = FirebaseMessaging.getInstance().send(message);
            log.info("Received answer: {}", response);
        } catch (FirebaseMessagingException e) {
            log.error("Error during FCM sending: {}", e.getMessage());
        }
    }

    public void updateAirRaidStatus() {
        List<Alert> responseList = airRaidProvider
                .get()
                .uri("/v3/alerts/" + properties.regionId())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (responseList == null) {
            log.error("Error: Failed to get valid response list");
            airRaidCache.put(AlertType.AIR, AlertStatus.UNKNOWN);
            return;
        }
        Alert response = responseList.getFirst();
        if (response == null) {
            log.error("Failed to get valid response");
            airRaidCache.put(AlertType.AIR, AlertStatus.UNKNOWN);
            return;
        }
        if (response.activeAlerts() == null) {
            log.error("Failed to get valid response with alertLevels");
            airRaidCache.put(AlertType.AIR, AlertStatus.UNKNOWN);
            return;
        }
        List<Alert.AlertLevelWithReason> activeAlerts = response.activeAlerts();
        for (Alert.AlertLevelWithReason activeAlert : activeAlerts) {
            AlertType type = AlertType.valueOf(activeAlert.type());
            AlertStatus current = airRaidCache.getIfPresent(type);
            AlertStatus updated = AlertStatus.maxLevel(activeAlert.activeAlertLevels().stream()
                    .map(Alert.AlertLevel::alertLevel)
                    .toList());
            if (current != updated) {
                airRaidCache.put(type, updated);
                if (properties.sendNotificationOnUpdate()) {
                    sendNotification(type);
                }
            }
            log.debug("Update alert status{}: {}", type, updated);
        }
    }

    public AlertStatus getStatus(AlertType type) {
        return airRaidCache.getIfPresent(type);
    }

    public void setStatus(AlertType type, AlertStatus status) {
        airRaidCache.put(type, status);
    }
}
