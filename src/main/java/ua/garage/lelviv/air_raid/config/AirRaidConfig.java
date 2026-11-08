package ua.garage.lelviv.air_raid.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.air_raid.entities.AlertStatus;
import ua.garage.lelviv.air_raid.entities.AlertType;

@Configuration
@EnableConfigurationProperties(AirRaidProperties.class)
public class AirRaidConfig {
    @Bean
    public Cache<AlertType, AlertStatus> airRaidCache() {
        return Caffeine.newBuilder().maximumSize(64).build();
    }

    @Bean
    public RestClient airRaidProvider(AirRaidProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.connectionTimeout());
        factory.setReadTimeout(properties.readTimeout());

        return RestClient.builder()
                .requestFactory(factory)
                .baseUrl(properties.baseUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, properties.secretKey())
                .build();
    }
}
