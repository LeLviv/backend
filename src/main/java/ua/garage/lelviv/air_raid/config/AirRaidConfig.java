package ua.garage.lelviv.air_raid.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@Configuration
@EnableConfigurationProperties(AirRaidProperties.class)
@EnableCaching
public class AirRaidConfig {
    @Bean
    public Cache<AlertType, AlertStatus> airRaidCache() {
        return Caffeine.newBuilder().maximumSize(64).build();
    }

    @Bean
    public RestClient airRaidProvider(AirRaidProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, properties.secretKey())
                .build();
    }
}
