package ua.garage.lelviv.air_raid;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(AirRaidProperties.class)
@EnableCaching
public class AirRaidConfig {
    public static final String lvivUID = "845";

    @Bean
    public Cache<String, AirRaidStatus> airRaidCache() {
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
