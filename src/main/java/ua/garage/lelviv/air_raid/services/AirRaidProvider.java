package ua.garage.lelviv.air_raid.services;

import java.util.List;
import ua.garage.lelviv.air_raid.entities.Alert;

public interface AirRaidProvider {
    List<Alert> getAlerts();
}
