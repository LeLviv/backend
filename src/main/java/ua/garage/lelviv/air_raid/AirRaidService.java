package ua.garage.lelviv.air_raid;

import com.github.benmanes.caffeine.cache.Cache;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AirRaidService {
    private final Cache<String, AirRaidStatus> airRaidCache;

    @PostConstruct
    public void initAirRaidStatus() {
        System.err.println("Set air raid status after init");
        airRaidCache.put("STATUS", AirRaidStatus.UNKNOWN);
    }

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

    public int dummyMethod() {
        int four = 2 * 2;
        return four;
    }
}
