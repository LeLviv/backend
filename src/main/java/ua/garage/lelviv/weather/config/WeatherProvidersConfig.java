package ua.garage.lelviv.weather.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.client.RestClient;
import ua.garage.lelviv.weather.provider.WeatherProvider;
import ua.garage.lelviv.weather.provider.openMeteo.OpenMeteoMapper;
import ua.garage.lelviv.weather.provider.openMeteo.OpenMeteoProvider;

@Slf4j
@Configuration
@EnableConfigurationProperties(WeatherProvidersProperties.class)
public class WeatherProvidersConfig {

    @Bean
    @Order(1)
    @ConditionalOnProperty(
            prefix = "spring.application.weather.providers.open-meteo",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true)
    public WeatherProvider openMeteoProvider(
            @Qualifier("openMeteoRestClient") RestClient restClient, OpenMeteoMapper openMeteoMapper) {
        log.info("Initializing OpenMeteoProvider Bean");
        return new OpenMeteoProvider(restClient, openMeteoMapper);
    }
}
