package ua.garage.lelviv.weather.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties(prefix = "app.weather.cache")
public record WeatherCacheProperties(
        @NotNull @Valid CacheProp daily,

        @NotNull @Valid CacheProp hourly,

        @NotNull @Valid CacheProp weekly,

        @NotNull @Valid CacheProp fallback) {

    public record CacheProp(
            @NotBlank String name,

            @NotNull @DefaultValue("10m") Duration ttl,

            @NotNull @DefaultValue("5000") int maxSize) {}
}
