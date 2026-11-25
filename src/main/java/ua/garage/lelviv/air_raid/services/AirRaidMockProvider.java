package ua.garage.lelviv.air_raid.services;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import ua.garage.lelviv.air_raid.entities.Alert;

@Component
@ConditionalOnProperty(name = "app.air-raid.enabled", havingValue = "false", matchIfMissing = true)
@Slf4j
public class AirRaidMockProvider implements AirRaidProvider {
    @Override
    public List<Alert> getAlerts() {
        return List.of();
    }
}
