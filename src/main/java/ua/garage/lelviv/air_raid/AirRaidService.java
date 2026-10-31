package ua.garage.lelviv.air_raid;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirRaidService {
    private final RestClient airRaidProvider;
    private final Cache<String, AirRaidStatus> airRaidCache;
    private final AirRaidProperties properties;

    public void sendNotification() {
        // TODO: Send message to firebase
        AirRaidStatus s = airRaidCache.getIfPresent("STATUS");
        if (s == null) {
            System.err.println("Error: Could not fetch airRaidStatus from cache.");
            return;
        }
        String m = switch (s) {
            case RED -> "Червоний рівень небезпеки";
            case YELLOW -> "Жовтий рівень небезпеки";
            case NONE -> "Відбій";
            case UNKNOWN -> "Немає інформації";
        };

        Notification notification = Notification.builder().setBody(m).build();

        Message message = Message.builder()
                .setTopic("air_raid_alert")
                .setNotification(notification)
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error during FCM sending: " + e.getMessage());
        }
    }

    public void updateAirRaidStatus() {
        List<Alert> responseList = airRaidProvider
                .get()
                .uri("/v3/alerts/" + properties.regionId())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<List<Alert>>() {});

        log.info("City id: {}", properties.regionId());
        if (responseList == null) {
            System.err.println("Error: Failed to get valid response list");
            airRaidCache.put("STATUS", AirRaidStatus.UNKNOWN);
            return;
        }
        Alert response = responseList.getFirst();
        if (response == null) {
            log.error("Failed to get valid response");
            airRaidCache.put("STATUS", AirRaidStatus.UNKNOWN);
            return;
        }
        if (response.activeAlerts() == null) {
            log.error("Failed to get valid response with alertLevels");
            airRaidCache.put("STATUS", AirRaidStatus.UNKNOWN);
            return;
        }
        List<Alert.AlertLevelWithReason> activeAlerts = response.activeAlerts();
        boolean changed = false;
        for (var a : activeAlerts) {
            log.info("{}", a);
            if (a.().equals("RED")) {
                airRaidCache.put("STATUS", AirRaidStatus.RED);
                return;
            }
            if (a.alertLevel().equals("YELLOW")) {
                changed = true;
                airRaidCache.put("STATUS", AirRaidStatus.YELLOW);
            }
        }
        if (!changed) {
            airRaidCache.put("STATUS", AirRaidStatus.NONE);
        }
    }

    public AirRaidStatus getStatus() {
        return airRaidCache.getIfPresent("STATUS");
    }

    public void setStatus(AirRaidStatus status) {
        airRaidCache.put("STATUS", status);
    }
}
