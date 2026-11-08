package ua.garage.lelviv.air_raid.services;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.EnumMap;
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
import ua.garage.lelviv.air_raid.exception.AlertStatusUpdateException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AirRaidService {
    private final RestClient airRaidProvider;
    private final Cache<AlertType, AlertStatus> airRaidCache;
    private final AirRaidProperties properties;
    private final NotificationService notificationService;

    public void updateAirRaidStatus() {
        List<Alert> responseList = null;
        try {
            responseList = airRaidProvider
                    .get()
                    .uri("/v3/alerts/" + properties.regionId())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {});
        } catch (Exception e) {
            for (AlertType type : AlertType.values()) {
                airRaidCache.put(type, AlertStatus.UNKNOWN);
            }
            throw new AlertStatusUpdateException("Failed to fetch alerts from API\n" + e);
        }

        if (responseList == null) {
            airRaidCache.put(AlertType.AIR, AlertStatus.UNKNOWN);
            throw new AlertStatusUpdateException("Missing response");
        }
        if (responseList.isEmpty()) {
            airRaidCache.put(AlertType.AIR, AlertStatus.UNKNOWN);
            throw new AlertStatusUpdateException("Response is invalid");
        }
        Alert response = responseList.getFirst();
        if (response.activeAlerts() == null) {
            airRaidCache.put(AlertType.AIR, AlertStatus.UNKNOWN);
            throw new AlertStatusUpdateException("Missing activeAlerts from response");
        }
        List<Alert.AlertLevelWithReason> activeAlerts = response.activeAlerts();

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
            if (current == null) {
                current = AlertStatus.NONE;
            }
            AlertStatus updated = activeMap.getOrDefault(type, AlertStatus.NONE);
            if (updated != current) {
                airRaidCache.put(type, updated);
                if (properties.sendNotificationOnUpdate()
                        && !(current == AlertStatus.UNKNOWN && updated == AlertStatus.NONE)) {
                    notificationService.sendNotification(type, updated);
                }
                log.info("Updated alert status of type {}: {}", type, updated);
            }
        }
    }

    public AlertStatus getStatus(AlertType type) {
        return airRaidCache.getIfPresent(type);
    }

    public void setStatus(AlertType type, AlertStatus status) {
        airRaidCache.put(type, status);
    }
}
