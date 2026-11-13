package ua.garage.lelviv.air_raid.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.firebase.enabled", havingValue = "false", matchIfMissing = true)
public class MockNotificationService implements NotificationService {
    @Override
    public void sendNotification(AlertType type, AlertStatus status) {
        log.info("Sending notification to Firebase {Type: {}, Status: {}}", type, status);
    }
}
