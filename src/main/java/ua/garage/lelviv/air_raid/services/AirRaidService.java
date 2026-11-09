package ua.garage.lelviv.air_raid.services;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.air_raid.config.AirRaidProperties;
import ua.garage.lelviv.air_raid.entities.Alert;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;
import ua.garage.lelviv.air_raid.exception.AlertStatusUpdateException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirRaidService {
    private final AirRaidProvider airRaidProvider;
    private final Cache<AlertType, AlertStatus> airRaidCache;
    private final AirRaidProperties properties;
    private final NotificationService notificationService;

    public void updateAirRaidStatus() {
        List<Alert.AlertLevelWithReason> activeAlerts = getActiveAlerts();

        Map<AlertType, AlertStatus> activeMap = new EnumMap<>(AlertType.class);
        for (Alert.AlertLevelWithReason activeAlert : activeAlerts) {
            AlertType type = AlertType.valueOf(activeAlert.type());
            AlertStatus updated = AlertStatus.maxLevel(activeAlert.activeAlertLevels().stream()
                    .map(Alert.AlertLevel::alertLevel)
                    .toList());
            activeMap.put(type, updated);
        }

        for (AlertType type : AlertType.values()) {
            AlertStatus current = airRaidCache.getIfPresent(type);
            AlertStatus effectiveCurrent = (current != null) ? current : AlertStatus.NONE;
            AlertStatus updated = activeMap.getOrDefault(type, AlertStatus.NONE);

            if (updated != effectiveCurrent) {
                airRaidCache.put(type, updated);
                if (properties.sendNotificationOnUpdate()) {
                    notificationService.sendNotification(type, updated);
                }
                log.info("Updated alert status of type {}: {}", type, updated);
            }
        }
    }

    private @NonNull List<Alert.AlertLevelWithReason> getActiveAlerts() {
        List<Alert> responseList = null;
        try {
            responseList = airRaidProvider.getAlerts();
        } catch (Exception e) {
            throw new AlertStatusUpdateException("Failed to fetch alerts from API\n" + e);
        }

        if (responseList == null) {
            throw new AlertStatusUpdateException("Missing response");
        }
        if (responseList.isEmpty()) {
            throw new AlertStatusUpdateException("Response is invalid");
        }
        Alert response = responseList.getFirst();
        if (response.activeAlerts() == null) {
            throw new AlertStatusUpdateException("Missing activeAlerts from response");
        }
        return response.activeAlerts();
    }

    public AlertStatus getStatus(AlertType type) {
        AlertStatus status = airRaidCache.getIfPresent(type);
        return status != null ? status : AlertStatus.NONE;
    }

    public void setStatus(AlertType type, AlertStatus status) {
        airRaidCache.put(type, status);
    }
}
