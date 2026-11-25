package ua.garage.lelviv.air_raid.services;

import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

public interface NotificationService {
    void sendNotification(AlertType type, AlertStatus status);
}
