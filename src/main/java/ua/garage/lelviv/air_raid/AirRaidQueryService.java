package ua.garage.lelviv.air_raid;

import static ua.garage.lelviv.air_raid.AirRaidConfig.lvivUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@ConditionalOnProperty(name = "spring.application.air-raid.api-query-enabled", havingValue = "true")
public class AirRaidQueryService {
    private final RestClient airRaidAPI;

    public AirRaidQueryService(RestClient.Builder clientBuilder) {
        this.airRaidAPI = clientBuilder
                .baseUrl("https://api.alerts.in.ua")
                .defaultHeader("Authorization", "Bearer ${air_raid_token}")
                .build();
    }

    @Scheduled(fixedDelay = 15000)
    public void updateAirRaidStatus() {

        var content =
                airRaidAPI.get().uri("/v2/iot/" + lvivUID + ".json").retrieve().body(String.class);

        // TODO: Add error handling, properly update status
    }
}
