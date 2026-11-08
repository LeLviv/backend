package ua.garage.lelviv.air_raid.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.garage.lelviv.air_raid.AirRaidService;

@Service
@ConditionalOnProperty(name = "app.air-raid.api-query-enabled", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class AirRaidScheduledQuery {
    private final AirRaidService service;

    @Scheduled(fixedRateString = "${app.air-raid.rate:61000}")
    public void scheduledUpdate() {
        log.info("Querying air raid status");
        service.updateAirRaidStatus();
    }
}
