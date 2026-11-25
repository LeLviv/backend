package ua.garage.lelviv.air_raid.services;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.air_raid.config.AirRaidProperties;
import ua.garage.lelviv.air_raid.entities.Alert;

@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "app.air-raid.enabled", havingValue = "true")
@Slf4j
public class AirRaidAPIProvider implements AirRaidProvider {
    private final RestClient airRaidRestClient;
    private final AirRaidProperties properties;

    @Override
    public List<Alert> getAlerts() {
        return airRaidRestClient
                .get()
                .uri("/v3/alerts/" + properties.regionId())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
