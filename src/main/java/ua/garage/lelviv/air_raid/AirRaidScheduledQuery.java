package ua.garage.lelviv.air_raid;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "app.air-raid.api-query-enabled", havingValue = "true")
@RequiredArgsConstructor
public class AirRaidScheduledQuery {
    private final AirRaidService service;

    @Scheduled(fixedDelay = 61000)
    public void scheduledUpdate() {
        System.out.println("Updating air raid status from official API");
        service.updateAirRaidStatus();
    }
}
