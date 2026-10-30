package ua.garage.lelviv.air_raid;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class AirRaidConfig {
    public static final String lvivUID = "845";

    @Bean
    public Cache<String, AirRaidStatus> airRaidCache() {
        return Caffeine.newBuilder().maximumSize(64).build();
    }

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}
