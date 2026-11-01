package ua.garage.lelviv.weather.config;

import jakarta.validation.constraints.NotNull;
import java.time.ZoneId;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app.weather")
public record WeatherBaseProperties(
        @NotNull @URL String baseErrorsUrl,
        @DefaultValue("Europe/Kyiv") ZoneId defaultTimezone) {}
