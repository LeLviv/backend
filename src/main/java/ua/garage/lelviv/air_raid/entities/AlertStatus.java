package ua.garage.lelviv.air_raid.entities;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public enum AlertStatus {
    UNKNOWN,
    NONE,
    YELLOW,
    RED;

    public static AlertStatus maxLevel(List<String> levels) {
        return levels.stream()
                .map(AlertStatus::parseString)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(AlertStatus.NONE);
    }

    private static AlertStatus parseString(String level) {
        try {
            return AlertStatus.valueOf(level.trim().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return AlertStatus.UNKNOWN;
        }
    }
}
