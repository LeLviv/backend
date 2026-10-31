package ua.garage.lelviv.weather.config;

import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.weather")
public record WeatherBaseProperties(@NotNull @URL String baseErrorsUrl) {}
