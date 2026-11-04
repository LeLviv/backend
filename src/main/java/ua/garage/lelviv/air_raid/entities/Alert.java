package ua.garage.lelviv.air_raid.entities;

import java.util.List;

public record Alert(
        String regionId,
        String regionType,
        String regionName,
        String regionEngName,
        String lastUpdate,
        List<AlertLevelWithReason> activeAlerts) {

    public record AlertLevelWithReason(
            String regionId,
            String regionType,
            String type,
            String regionEngName,
            String lastUpdate,
            List<AlertLevel> activeAlertLevels) {}

    public record AlertLevel(String alertLevel, String reason, String createdAt) {}
}
