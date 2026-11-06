package ua.garage.lelviv.weather.config;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.weather")
public record WeatherBaseProperties(
        @DefaultValue("Europe/Kyiv") String defaultTimezone) {}
